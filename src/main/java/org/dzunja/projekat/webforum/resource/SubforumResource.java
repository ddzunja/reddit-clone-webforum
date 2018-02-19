package org.dzunja.projekat.webforum.resource;

import java.io.File;
import java.io.InputStream;
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

import org.dzunja.projekat.webforum.model.Subforum;
import org.dzunja.projekat.webforum.service.SubforumService;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

@Path("subforums")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SubforumResource {
	
	private SubforumService subforumService = new SubforumService();
	
	@GET
	public Response get(@Context UriInfo uriInfo) {
		
		List<Subforum> list = subforumService.get();
		
		if(list != null) {
			
			return Response.status(Status.ACCEPTED)
						   .entity(list)
						   .build();
			
		}
			
		return Response.status(Status.INTERNAL_SERVER_ERROR)
					   .build();
			
	
	}
	
	@GET
	@Path("{subforumId}")
	public Response get(@PathParam("subforumId") String subforumId) {
		
		
		Subforum subforum = subforumService.get()
										   .stream()
										   .filter(s -> s.getSubForumId().equals(subforumId))
										   .findFirst().orElse(null);
		
		
		if(subforum != null) {
			return Response.status(Status.OK)
					       .entity(subforum)
					       .build();
			
			
		}
			
		return Response.status(Status.NOT_FOUND)
					   .build();
	
		
	}
	
	@POST
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response post(@FormDataParam("subForumId") String subForumId, @FormDataParam("description") String description,
			@FormDataParam("image") InputStream uploadedInputStream, @FormDataParam("image") FormDataContentDisposition fileDescription,
			@FormDataParam("listOfRules") List<String> listOfRules, @FormDataParam("mainModId") String mainModId,
			@FormDataParam("moderatorsIds") List<String> moderatorsIds, @Context UriInfo uriInfo) {
		/// list of rules nesto ne radi
		Subforum newSubforum = new Subforum();
		
		if(fileDescription != null) {
			String imgPath = subforumService.imageFolderLocation + fileDescription.getFileName();
			subforumService.writeToFile(uploadedInputStream, imgPath);
			
			String imgRelativePath = new File(subforumService.relativeLocalHostPath).toURI()
																					.relativize(new File(imgPath).toURI())
																					.getPath();
			newSubforum.setIconPath(imgRelativePath);
		}else {
			newSubforum.setIconPath("icons/empty.png");
		}
		
		newSubforum.setSubForumId(subForumId);
		newSubforum.setDescription(description);
		newSubforum.setListOfRules(listOfRules);
		newSubforum.setMainModId(mainModId);
		newSubforum.setModeratorsIds(moderatorsIds);
		
		String uri = uriInfo.getPath() + '/' + newSubforum.getSubForumId();
		newSubforum.addLink(uri, "self");
		
		
		if(subforumService.add(newSubforum) != null) {
			
			
			
			return Response.status(Status.CREATED)
						   .entity(newSubforum)
						   .build();
		}
		
		return Response.status(Status.CONFLICT)
					   .build();
		
	
	}
	
	@PUT
	@Path("{subforumId}")
	public Response put(@PathParam("subforumId") String subforumId, Subforum updateSubforum) {
		
		if(subforumId.equals(updateSubforum.getSubForumId())){
			
			if(subforumService.update(updateSubforum) != null) {
				
				return Response.status(Status.OK)
							   .entity(updateSubforum)
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
	public Response search(@QueryParam("subforumId") String subforumId, @QueryParam("description") String description, 
			@QueryParam("mainModId") String mainMod) {
		
		
		List<Subforum> list = subforumService.get();
		
		List<Subforum> found = list.stream().filter(s -> subforumService.isBadString(subforumId) ||  s.getSubForumId().equals(subforumId))
											.filter(s -> subforumService.isBadString(description)  || s.getDescription().equals(description))
											.filter(s -> subforumService.isBadString(mainMod)  || s.getMainModId().equals(mainMod))
											.collect(Collectors.toList());
		
		if(list != null && !list.isEmpty()) {
			
			return Response.status(Status.OK)
						   .entity(found)
						   .build();
			
		}
		
		return Response.status(Status.NO_CONTENT)
					   .build();
	}
	
	
	@DELETE
	@Path("{subforumId}")
	public Response delete(@PathParam("subforumId") String subforumId) {
		
		Subforum temp = new Subforum();
		temp.setSubForumId(subforumId);
		Subforum delSubforum = subforumService.remove(temp);
		
		if(delSubforum != null) {
			
			return Response.status(Status.OK)
						   .entity(delSubforum)
						   .build();
		}
		
		return Response.status(Status.NOT_FOUND)
				  	   .build();
		
	}
	
	
}
