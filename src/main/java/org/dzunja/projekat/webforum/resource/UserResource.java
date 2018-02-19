package org.dzunja.projekat.webforum.resource;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
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

import org.dzunja.projekat.webforum.model.User;
import org.dzunja.projekat.webforum.model.User.Role;
import org.dzunja.projekat.webforum.service.UserService;


@Path("users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserResource {
	
	public static String LOGGED_USER = "logged_user";
	private UserService userService = new UserService();
	
	@GET
	public Response get(){
		
		List<User> users = userService.get();
		
			if(users != null) {
				
				return Response.status(Status.ACCEPTED)
							   .entity(users)
							   .build();
		
			}
				
			return Response.status(Status.INTERNAL_SERVER_ERROR)
						   .build();
			
	}
	
	@GET
	@Path("{username}")
	public Response get(@PathParam("username") String username, @Context UriInfo uriInfo) {
		
		User user = userService.getMap().get(username);
		
		if(user != null) {
			String uri = uriInfo.getBaseUriBuilder()
								.path(UserResource.class)
								.path(user.getUsername())
								.build()
								.toString();
			user.addLink(uri, "self");
								
			return Response.status(Status.OK)
						   .entity(user)
						   .build();
		}
		
		return Response.status(Status.NOT_FOUND)
					   .build();
		
	
	}
	
	@POST
	public Response post(User newUser, @Context UriInfo uriInfo) {
	
		
		if(userService.add(newUser) != null) {
			
			URI uri = uriInfo.getAbsolutePathBuilder().path(newUser.getUsername()).build();
			
			return Response.created(uri)
						   .entity(newUser)
						   .build();
			
		}
			
		return Response.status(Status.CONFLICT)
					   .build();
		
		
	}
	
	@PUT
	@Path("{username}")
	public Response put(@PathParam("username") String username, User updateUser) {
		
		if(username.equals(updateUser.getUsername())) {
			
			if(userService.update(updateUser) != null) {
				
				return Response.status(Status.OK)
							   .entity(updateUser)
							   .build();
					
			}else {
				
				return Response.status(Status.BAD_REQUEST)
							   .build();
			}
				
		}
			
		
		return Response.status(Status.PRECONDITION_FAILED)
					   .build();
			
		
		
	}
	
	@DELETE
	@Path("{username}")
	public Response delete(@PathParam("username") String username) {
		
		User temp = new User();
		temp.setUsername(username);
		User delUser = userService.remove(temp);
		
		if(delUser != null) {
			
			return Response.status(Status.OK)
						   .entity(delUser)
						   .build();
			
		}
			
		return Response.status(Status.NOT_FOUND)
					   .build();
	
	}
	
	@GET
	@Path("getAllModsAdmins")
	public Response getModAdmin() {
		
		List<String> list = userService.get()
									   .stream()
									   .filter(user -> { 
										   return user.getRole().equals(Role.ADMIN) || 
												  user.getRole().equals(Role.MOD); 
									   })
									   .map(user -> user.getUsername())
									   .collect(Collectors.toList());
		
		
		return Response.status(Status.OK)
					   .entity(list)
					   .build();
	
	}
	
	
	@GET
	@Path("search")
	public Response search(@QueryParam("username") String username) {
		
		List<User> list = userService.get();
		
		User found = list.stream()
						 .filter(u -> u.getUsername().equals(username))
						 .findFirst().orElse(null);
		
		if(found != null) {
			return Response.status(Status.OK)
						   .entity(found)
						   .build();
		}
		
		return Response.status(Status.NO_CONTENT)
					   .build();
		
	}
	
	
	
	@POST
	@Path("login")
	public Response login(User loginUser, @Context HttpServletRequest req) {
		
		if(loginUser != null) {
			User getUser = userService.userAuthentication(loginUser.getUsername(), loginUser.getPassword());
			
			if(getUser != null) {
				
				HttpSession session = req.getSession(true);
				session.setAttribute(LOGGED_USER, getUser);
				
				return Response.status(Status.ACCEPTED)
							   .entity(getUser)
							   .build();
			}
			
		}
		
		
		return Response.status(Status.UNAUTHORIZED)
					   .build();
		
	}
	
	@GET
	@Path("getSessionUser")
	public Response checkUser(@Context HttpServletRequest req) {
		
		HttpSession session = req.getSession(false);
		/// salje se novi zapisani user uvek!
		if(session != null) {
			User getSessionUser = (User)session.getAttribute(LOGGED_USER);
			
			if(getSessionUser != null) {
				User newUser = userService.userAuthentication(getSessionUser.getUsername(), getSessionUser.getPassword());
				
				System.out.println("User is logged in! ");
				System.out.println(newUser);
				System.out.println("----------------------------------");
				
				if(newUser != null) {
				
				
				return Response.status(Status.ACCEPTED)
							   .entity(newUser)
							   .build();
			}
		}
		}
		
		return Response.status(Status.UNAUTHORIZED)
					   .build();
	}
	
	@GET
	@Path("logout")
	public Response logout(@Context HttpServletRequest req) {
		
		System.out.println("Logout");
		HttpSession session = req.getSession(false);
		
		if(session != null) 
			session.invalidate();
			
		return Response.status(Status.ACCEPTED)
					   .build();
		
	}
	
	

}
