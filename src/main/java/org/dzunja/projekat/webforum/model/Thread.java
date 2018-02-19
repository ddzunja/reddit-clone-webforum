package org.dzunja.projekat.webforum.model;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class Thread extends Model {
	
	public enum ThreadType {TEXT, IMAGE, LINK};
	
	private String subforumId;
	private String nameId;
	private ThreadType type;
	private String authorId;
	private List<String> comments;
	private String content;
	private String creationDate;
	private List<String> userLikeIds;
	private List<String> userDislikeIds;
	
	public Thread() { 
		
		this.creationDate = new SimpleDateFormat(Model.calendarString).format(new Date());
	}

	public ThreadType getType() {
		return type;
	}

	public void setType(ThreadType type) {
		this.type = type;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getSubforumId() {
		return subforumId;
	}

	public String getNameId() {
		return nameId;
	}

	public String getAuthorId() {
		return authorId;
	}
	
	public void setAuthorId(String authorId) {
		this.authorId = authorId;
	}

	public void setComments(List<String> comments) {
		this.comments = comments;
	}

	public void setUserLikeIds(List<String> userLikeIds) {
		this.userLikeIds = userLikeIds;
	}

	public void setUserDislikeIds(List<String> userDislikeIds) {
		this.userDislikeIds = userDislikeIds;
	}

	public String getCreationDate() {
		return creationDate;
	}
	
	public List<String> getComments() {
		return comments == null ? comments = new ArrayList<String>() : comments;
	}

	public List<String> getUserLikeIds() {
		return userLikeIds == null ? userLikeIds = new ArrayList<String>() : userLikeIds;
	}

	public List<String> getUserDislikeIds() {
		return userDislikeIds == null ? userDislikeIds = new ArrayList<String>() : userDislikeIds;
	}

	public void setSubforumId(String subforumId) {
		this.subforumId = subforumId;
	}

	public void setNameId(String nameId) {
		this.nameId = nameId;
	}

	public void setCreationDate(String creationDate) {
		this.creationDate = creationDate;
	}
	

}

