package com.example.demo.model.dto;

public class SkillView{
	
	private int id;
	private String userName;
	private String skill;
	
	public SkillView(Integer id, String userName, String skill) {
		this.id = id;
		this.userName = userName;
		this.skill = skill;
	}
	
	public int getId() {
		return id;
	}
	
	public String getUserName() {
		return userName;
	}
	
	public String getSkill() {
		return skill;
	}
}