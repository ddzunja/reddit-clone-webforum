package org.dzunja.projekat.webforum.service;

import java.util.Map;
import java.util.stream.Collectors;

import org.dzunja.projekat.webforum.model.User;

public class UserService extends AbstractService<User>{
	
	public static final String dataFileName = "users.json";
	
	public UserService() {
		super(dataFileName, User.class);
	}


	
	public Map<String, User> getMap() {
		return get().stream()
					.collect(Collectors
					.toMap(User::getUsername, user -> user));		
	}


	@Override
	public User add(User item) {
		
		Map<String, User> map = getMap();
		
		item.setUsername(item.getUsername().toLowerCase());
		if(!map.containsKey(item.getUsername())) {
			map.put(item.getUsername(), item);
			update(map);
			return item;
		}
		
		return null;
	}


	@Override
	public User update(User item) {
		
		Map<String, User> map = getMap();
		
		if(map.containsKey(item.getUsername())) {
			map.put(item.getUsername(), item);
			update(map);
			return item;
		}
		
		return null;
	}


	
	public User remove(User item) {
		
		Map<String, User> map = getMap();
		
		if(map.containsKey(item.getUsername())) {
			User delUser = map.get(item.getUsername());
			delUser.setDeleted(true);
			update(map);
			return delUser;
		}
		
		return null;
	}
	
	public User userAuthentication(String username, String password) {
		Map<String, User> map = getMap();
		
		if(map.containsKey(username)) {
			if(map.get(username).getPassword().equals(password)) {
				return map.get(username);
			}
		}
		
		return null;

	}
	
	

}

