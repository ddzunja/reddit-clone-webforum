package org.dzunja.projekat.webforum.resource;

import java.util.List;
import java.util.stream.Collectors;

import javax.ws.rs.Consumes;
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

import org.dzunja.projekat.webforum.model.Message;
import org.dzunja.projekat.webforum.service.MessageService;

@Path("messages")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MessageResource {
	
	private MessageService messageService = new MessageService();

	@GET
	@Path("sender/{username}")
	public Response getSenderMessages(@PathParam("username") String username) {
		
		List<Message> list = messageService.get()
										   .stream()
										   .filter(m -> m.getSender().equals(username))
										   .collect(Collectors.toList());
		return Response.status(Status.OK)
					  .entity(list)
					  .build();
	}
	
	@GET
	@Path("receiver/{username}")
	public Response getReceiverMessages(@PathParam("username") String username) {
		
		List<Message> list = messageService.get()
										   .stream()
										   .filter(m -> m.getReceiver().equals(username))
										   .collect(Collectors.toList());
		return Response.status(Status.OK)
					  .entity(list)
					  .build();
	}
	
	@POST
	@Path("send")
	public Response sendMessage(Message newMessage, @Context UriInfo uriInfo) {
		
		messageService.add(newMessage);
		
		return Response.status(Status.CREATED)
					   .entity(newMessage)
					   .build();
	}
	
	@PUT
	@Path("seen")
	public Response seenMessage(Message newMessage) {
		
		Message m = messageService.update(newMessage);
		
		if(m != null) {
			
			return Response.status(Status.OK)
						   .entity(m)
						   .build();
			
		}
		
		return Response.status(Status.METHOD_NOT_ALLOWED)
					   .build();
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
