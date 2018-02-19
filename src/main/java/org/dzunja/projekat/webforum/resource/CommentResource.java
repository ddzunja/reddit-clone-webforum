package org.dzunja.projekat.webforum.resource;

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
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.dzunja.projekat.webforum.model.Comment;

import org.dzunja.projekat.webforum.service.CommentService;

@Path("subforums/{subforumId}/threads/{threadId}/comments")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CommentResource {
	
	private CommentService commentService = new CommentService();
	
	@GET
	public Response get(@PathParam("subforumId") String subforumId, @PathParam("threadId") String nameId) {
		
		List<Comment> list = commentService.get()
										   .stream()
										   .filter(c -> c.getSubforumId().equals(subforumId))
										   .filter(c -> c.getThreadId().equals(nameId))
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
	@Path("{commentId}")
	public Response get(@PathParam("subforumId") String subforumId, @PathParam("threadId") String nameId, 
			@PathParam("commentId") Integer commentId) {
		
		Comment comment = commentService.get()
				   						.stream()
				   						.filter(c -> c.getSubforumId().equals(subforumId))
				   						.filter(c -> c.getThreadId().equals(nameId))
				   						.filter(c -> c.getCommentId().equals(commentId))
				   						.findFirst().orElse(null);
		
		if(comment != null) {
			
			return Response.status(Status.OK)
						   .entity(comment)
						   .build();
			
		}
		
		return Response.status(Status.NOT_FOUND)
					   .build();
		
		
	}
	
	@POST
	public Response post(Comment newComment, @PathParam("subforumId") String subforumId,
			@PathParam("threadId") String nameId, @Context UriInfo uriInfo) {
		
		newComment.setSubforumId(subforumId);
		newComment.setThreadId(nameId);
		newComment.addLink(uriInfo.getPath(), "self");
		
		
		if(commentService.add(newComment) != null) {
			
			
			
			URI uri = uriInfo.getAbsolutePathBuilder().path(newComment.getCommentId().toString())
													  .build();
			
			return Response.created(uri)
						   .entity(newComment)
						   .build();
			
		}
		
		return Response.status(Status.CONFLICT)
					   .build();
		
	}
	
	@PUT
	@Path("{commentId}")
	public Response put(@PathParam("subforumId") String subforumId, @PathParam("threadId") String nameId, 
			@PathParam("commentId") Integer commentId, Comment updateComment) {
		System.out.println("RADI?");
		
		if(updateComment.getSubforumId().equals(subforumId) && updateComment.getThreadId().equals(nameId)
				&& updateComment.getCommentId().equals(commentId)) {
			
			
			if(commentService.update(updateComment) != null) {
				
				return Response.status(Status.OK)
							   .entity(updateComment)
							   .build();
				
			} else {
			
				
				return Response.status(Status.BAD_REQUEST)
							   .build();
			}

		}
		
		return Response.status(Status.PRECONDITION_FAILED)
					   .build();
		
	}
	
	
	//logicko brisanje --- mozda je ne potrebno, moguce je preko update uraditi samo setDeleted
	@DELETE
	@Path("{commentId}")
	public Response delete(@PathParam("subforumId") String subforumId, @PathParam("threadId") String nameId, 
			@PathParam("commentId") Integer commentId) {
		System.out.println("Dejan");
		Comment temp = new Comment();
		temp.setSubforumId(subforumId);
		temp.setThreadId(nameId);
		temp.setCommentId(commentId);
		
		Comment delComment = commentService.logicalRemove(temp); 
		
		if(delComment != null) {
			
			return Response.status(Status.OK)
						   .entity(delComment)
						   .build();
			
		}
		
		return Response.status(Status.NOT_FOUND)
		  	   	   	   .build();
		
		
	}
	

}
