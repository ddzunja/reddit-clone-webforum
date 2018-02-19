package org.dzunja.projekat.webforum.model;

import java.util.ArrayList;
import java.util.List;

public class Subforum extends Model {
	
	private String subForumId;
	private String description;
	private String iconPath;
	private List<String> listOfRules;
	private String mainModId;
	
	private List<String> moderatorsIds;
	
	public Subforum() { }

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getIconPath() {
		return iconPath;
	}

	public void setIconPath(String iconPath) {
		this.iconPath = iconPath;
	}

	public String getMainModId() {
		return mainModId;
	}

	public void setMainModId(String mainModId) {
		this.mainModId = mainModId;
	}

	public String getSubForumId() {
		return subForumId;
	}

	
	public List<String> getListOfRules() {
		return listOfRules == null ? listOfRules = new ArrayList<String>() : listOfRules;
		
	}

	
	public List<String> getModeratorsIds() {
		return moderatorsIds == null ? moderatorsIds = new ArrayList<String>() : moderatorsIds;
		
	}

	public void setSubForumId(String subForumId) {
		this.subForumId = subForumId;
	}

	public void setListOfRules(List<String> listOfRules) {
		this.listOfRules = listOfRules;
	}

	public void setModeratorsIds(List<String> moderatorsIds) {
		this.moderatorsIds = moderatorsIds;
	}
	
}

