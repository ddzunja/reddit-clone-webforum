package org.dzunja.projekat.webforum.resource;

import java.io.File;
import java.io.InputStream;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;


import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;


import org.dzunja.projekat.webforum.model.Thread;

import org.dzunja.projekat.webforum.service.ThreadService;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

@Path("subforums/{subforumId}/threads")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ThreadResource {
	
	private ThreadService threadService = new ThreadService();
	
	@GET
	public Response get(@PathParam("subforumId") String subforumId, @Context UriInfo uriInfo) {
		
		List<Thread> list = threadService.get()
										 .stream()
										 .filter(t -> t.getSubforumId().equals(subforumId))
										 .collect(Collectors.toList());
		
		if(list != null) {
			
			return Response.status(Status.ACCEPTED)
						   .entity(list)
						   .build();
			
		}
		
		return Response.status(Status.INTERNAL_SERVER_ERROR)
				   	   .build();
		
	}
	
	@GET
	@Path("{threadId}")
	public Response get(@PathParam("subforumId") String subforumId, @PathParam("threadId") String nameId) {
		
		Thread thread = threadService.get()
									 .stream()
									 .filter(t -> t.getSubforumId().equals(subforumId))
									 .filter(t -> t.getNameId().equals(nameId))
									 .findFirst().orElse(null);
		
		if(thread != null) {
			
			return Response.status(Status.OK)
						   .entity(thread)
						   .build();
			
		}
		
		return Response.status(Status.NOT_FOUND)
				   	   .build();
									
	}
	
	@POST
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response post(@FormDataParam("subforumId") String subforumId, @FormDataParam("nameId") String nameId,
			@FormDataParam("type") Thread.ThreadType type, @FormDataParam("authorId") String authorId,
			@FormDataParam("text") String text, @FormDataParam("link") String link,
			@FormDataParam("image") InputStream uploadedInputStream, @FormDataParam("image") FormDataContentDisposition fileDescription, 
			@Context UriInfo uriInfo) {
		/// paziti da ne postavi novi thread na forum koji ne postoji!
		
		Thread newThread = new Thread();
		
		
		if(type.equals(Thread.ThreadType.TEXT)) {
			newThread.setContent(text);
		}else if(type.equals(Thread.ThreadType.LINK)) {
			newThread.setContent(link);
		}else if(type.equals(Thread.ThreadType.IMAGE)) {
			
			String imgPath = threadService.imageFolderLocation + fileDescription.getFileName();
			threadService.writeToFile(uploadedInputStream, imgPath);
			
			String imgRelativePath = new File(threadService.relativeLocalHostPath).toURI()
																				  .relativize(new File(imgPath).toURI())
																				  .getPath();
			newThread.setContent(imgRelativePath);
				
		}
		
		newThread.setSubforumId(subforumId);
		newThread.setNameId(nameId);
		newThread.setType(type);
		newThread.setAuthorId(authorId);
		newThread.getUserLikeIds();
		newThread.getUserDislikeIds();
		newThread.getComments();
		
		String selfLink = uriInfo.getPath() + '/' + newThread.getNameId();
		newThread.addLink(selfLink, "self");
		
		
		if(threadService.add(newThread) != null) {
			
			URI uri = uriInfo.getAbsolutePathBuilder().path(newThread.getNameId())
													  .build();
			
			
			
			return Response.created(uri)
						   .entity(newThread)
						   .build();
		}
		
		return Response.status(Status.CONFLICT)
				   	   .build();
	
	}
	
	
	@PUT
	@Path("{threadId}/votes")
	public Response put(Thread threadToUpdate) {
		
		
		if(threadService.update(threadToUpdate) != null) {
			return Response.status(Status.OK)
						   .entity(threadToUpdate)
						   .build();
		}
		
		return Response.status(Status.CONFLICT)
					   .build();
		
		
	}
	
	
	@PUT
	@Path("{threadId}")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response put(@PathParam("subforumId") String subforumIdPathParam, @PathParam("threadId") String nameIdPathParam, @FormDataParam("subforumId") String subforumId,
			@FormDataParam("nameId") String nameId, @FormDataParam("type") Thread.ThreadType type, @FormDataParam("authorId") String authorId,
			@FormDataParam("text") String text, @FormDataParam("link") String link, @FormDataParam("creationDate") String creationDate,
			@FormDataParam("userLikeIds") List<String> userLikeIds, @FormDataParam("userDislikeIds") List<String> userDislikeIds,
			@FormDataParam("image") InputStream uploadedInputStream, @FormDataParam("image") FormDataContentDisposition fileDescription,
			@Context UriInfo uriInfo) {
		
		
		if(subforumId.equals(subforumIdPathParam) && nameId.equals(nameIdPathParam)) {
			
			Thread updateThread = new Thread();
			
			if(type.equals(Thread.ThreadType.TEXT)) {
				updateThread.setContent(text);
			}else if(type.equals(Thread.ThreadType.LINK)) {
				updateThread.setContent(link);
			}else if(type.equals(Thread.ThreadType.IMAGE)) {
				
				String imgPath = threadService.imageFolderLocation + fileDescription.getFileName();
				threadService.writeToFile(uploadedInputStream, imgPath);
				
				String imgRelativePath = new File(threadService.relativeLocalHostPath).toURI()
																					  .relativize(new File(imgPath).toURI())
																					  .getPath();
				updateThread.setContent(imgRelativePath);
					
			}
			
			updateThread.setSubforumId(subforumId);
			updateThread.setNameId(nameId);
			updateThread.setType(type);
			updateThread.setAuthorId(authorId);
			updateThread.setCreationDate(creationDate);
			updateThread.setUserLikeIds(userLikeIds);
			updateThread.setUserDislikeIds(userDislikeIds);
			
			String selfLink = uriInfo.getPath();
			updateThread.addLink(selfLink, "self");
			
			
			if(threadService.update(updateThread) != null) {
				
				
				
				return Response.status(Status.OK)
							   .entity(updateThread)
							   .build();
				
			
			}else {
				
				return Response.status(Status.BAD_REQUEST)
						   	   .build();
			}
			
		}
		
		return Response.status(Status.PRECONDITION_FAILED)
				   	   .build();
		
	}
	
	
	@GET
	@Path("search")
	public Response search(@QueryParam("nameId") String nameId, @QueryParam("type") Thread.ThreadType type,
			@QueryParam("authorId") String authorId, @QueryParam("subforimId") String subforumId) {
		
		System.out.println("Tip" + type);
		
		List<Thread> list = threadService.get();
		
		List<Thread> found = list.stream().filter(t -> threadService.isBadString(nameId) || t.getNameId().equals(nameId))
										  .filter(t -> type == null || t.getType().equals(type))
										  .filter(t -> threadService.isBadString(authorId) || t.getAuthorId().equals(authorId))
										  .filter(t -> threadService.isBadString(subforumId) || t.getSubforumId().equals(subforumId))
										  .collect(Collectors.toList());
		
		if(found != null && !found.isEmpty()) {
			
			return Response.status(Status.OK)
						   .entity(found)
						   .build();
			
			
		}
		
		
		return Response.status(Status.NO_CONTENT)
					   .build();
	}
	
	@DELETE
	@Path("{threadId}")
	public Response delete(@PathParam("subforumId") String subforumId, @PathParam("threadId") String nameId) {
		
		Thread temp = new Thread();
		temp.setSubforumId(subforumId);
		temp.setNameId(nameId);
		
		Thread delThread = threadService.remove(temp);
		
		if(delThread != null) {
			
			return Response.status(Status.OK)
						   .entity(delThread)
						   .build();
			
		}
		
		return Response.status(Status.NOT_FOUND)
			  	   	   .build();
		
		
	}
	
	
	
	
	

}
