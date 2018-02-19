package org.dzunja.projekat.webforum.model;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class User extends Model {
	
	public enum Role {USER, MOD, ADMIN};
	
	private String username;
	private String password;
	private String name;
	private Role role;
	private String phone;
	private String email;
	private String date;
	private boolean deleted;
	private List<String> subscribedSubForumsIds;
	private List<Thread> subscribedThreadsIds;
	private List<Comment> savedCommentsIds;
	
	public User() {
		
		this.date = new SimpleDateFormat(Model.calendarString).format(new Date());
		this.deleted = false;
		this.role = Role.USER;
	}
	
	public boolean getDeleted() {
		return deleted;
	}
	
	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUsername() {
		return username;
	}

	public String getDate() {
		return date;
	}

	public List<String> getSubscribedSubForumsIds() {
		return subscribedSubForumsIds == null ? subscribedSubForumsIds = new ArrayList<String>() : subscribedSubForumsIds;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public void setSubscribedSubForumsIds(List<String> subscribedSubforumsIds) {
		this.subscribedSubForumsIds = subscribedSubforumsIds;
	}

	public void setSubscribedThreadsIds(List<Thread> subscribedThreadsIds) {
		this.subscribedThreadsIds = subscribedThreadsIds;
	}

	public void setSavedCommentsIds(List<Comment> savedCommentsIds) {
		this.savedCommentsIds = savedCommentsIds;
	}

	public List<Thread> getSubscribedThreadsIds() {
		return subscribedThreadsIds == null ? subscribedThreadsIds = new ArrayList<Thread>() : subscribedThreadsIds;
	}

	public List<Comment> getSavedCommentsIds() {
		return savedCommentsIds == null ? savedCommentsIds = new ArrayList<Comment>() : savedCommentsIds;
	}
	
	
	@Override
	public String toString() {
		return "\nUser [username=" + username + ", password=" + password + ", name=" + name + ", role=" + role
				+ ", phone=" + phone + ", email=" + email + ", date=" + date + ", deleted=" + deleted + ", subscribedSubforumsIds="
				+ subscribedSubForumsIds + ", subscribedThreadsIds=" + subscribedThreadsIds + ", savedCommentsIds="
				+ savedCommentsIds + "]";
	}
	

}

