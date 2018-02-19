package org.dzunja.projekat.webforum.model;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class Comment extends Model {

	private Integer commentId;
	private String subforumId;
	private String threadId;
	private String authorId;
	private String creationDate;
	private Integer parentCommentId;
	private String content;
	private List<String> userLikesIds;
	private List<String> userDislikeIds;
	private boolean edited;
	private boolean deleted;
	
	public Comment() {
		
		this.creationDate = new SimpleDateFormat(Model.calendarString).format(new Date());
		this.deleted = false;
		this.edited = false;
		
	}

	public Integer getCommentId() {
		return commentId;
	}
	
	public void setCommentId(Integer commentId) {
		this.commentId = commentId;
	}
	
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public boolean isEdited() {
		return edited;
	}

	public void setEdited(boolean isEdited) {
		this.edited = isEdited;
	}
	
	public boolean isDeleted() {
		return deleted;
	}
	
	public void setDeleted(boolean isDeleted) {
		this.deleted = isDeleted;
	}

	
	public void setUserLikesIds(List<String> userLikesIds) {
		this.userLikesIds = userLikesIds;
	}

	public void setUserDislikeIds(List<String> userDislikeIds) {
		this.userDislikeIds = userDislikeIds;
	}

	public String getThreadId() {
		return threadId;
	}

	public String getAuthorId() {
		return authorId;
	}

	public String getCreationDate() {
		return creationDate;
	}

	public Integer getParentCommentId() {
		return parentCommentId;
	}
	
	public String getSubforumId() {
		return subforumId;
	}

	public void setSubforumId(String subforumId) {
		this.subforumId = subforumId;
	}

	public void setThreadId(String threadId) {
		this.threadId = threadId;
	}

	public void setAuthorId(String authorId) {
		this.authorId = authorId;
	}

	public void setCreationDate(String creationDate) {
		this.creationDate = creationDate;
	}

	public void setParentCommentId(Integer parentCommentId) {
		this.parentCommentId = parentCommentId;
	}


	public List<String> getUserLikesIds() {
		return userLikesIds == null ? userLikesIds = new ArrayList<String>() : userLikesIds;
	}

	public List<String> getUserDislikeIds() {
		return userDislikeIds == null ? userDislikeIds = new ArrayList<String>() : userDislikeIds;
	}
	
	
}
