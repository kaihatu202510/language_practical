package com.example.demo.model.dto;

public class SkillDto {
	private Integer userId;
	private String skill;
	
	public SkillDto() {}
	
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
