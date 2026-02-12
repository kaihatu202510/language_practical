package com.example.demo.dto;

public class SkillRequest {
	private Integer userId;
	private String skill;
	
	public SkillRequest() {}
	
	public Integer getUserId() {
		return userId;
	}
	
	public String getSkill() {
		return skill;
	}
	
	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public void setSkill(String skill) {
		this.skill = skill;
	}
}
