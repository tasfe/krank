package com.arcmind.jpa.course.model;

public class UserInfo extends User {

	public UserInfo(Long id, String name, String groupName) {
		super(id, name);
		this.groupName = groupName;
	}
	public UserInfo () {
		
	}
	
	private String groupName;

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
}
