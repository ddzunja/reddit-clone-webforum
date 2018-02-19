package org.dzunja.projekat.webforum.model;

import java.util.ArrayList;

public abstract class Model {
	
	public static final String calendarString = "EEE, MMM d, ''yy 'at' HH:mm";
	
	protected ArrayList<Link> links = new ArrayList<Link>();
	
	public void addLink(String link, String rel) {
		links.add(new Link(link, rel));
	}
	
	public ArrayList<Link> getLinks(){
		return links;
	}
	
	public void setLinks(ArrayList<Link> links) {
		this.links = links;
	}
	

}
