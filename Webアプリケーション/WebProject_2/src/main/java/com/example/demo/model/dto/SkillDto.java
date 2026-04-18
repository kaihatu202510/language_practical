package com.example.demo.model.dto;

public class SkillDto {
	private Integer id;
	private Integer userId;
	private String name;
	
	public SkillDto() {}
	
	public Integer getId() {
		return id;
	}
	
	public Integer getUserId() {
		return userId;
	}
	
	public String getName() {
		return name;
	}
	
	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public void setSkill(String name) {
		this.name = name;
	}
}
