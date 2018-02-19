package org.dzunja.projekat.webforum.model;


public class Message extends Model {

	private Integer id;
	private String sender;
	private String receiver;
	private String content;
	private boolean seen;
	
	public Message() {
		this.seen = false;
	}
	

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public boolean isSeen() {
		return seen;
	}

	public void setSeen() {
		this.seen = true;
	}

	public String getReceiver() {
		return receiver;
	}

	public String getContent() {
		return content;
	}
	
	public Integer getId() {
		return id;
	}


	public void setId(Integer id) {
		this.id = id;
	}


	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}


	public void setContent(String content) {
		this.content = content;
	}

	public void setSeen(boolean seen) {
		this.seen = seen;
	}
	
	
}
