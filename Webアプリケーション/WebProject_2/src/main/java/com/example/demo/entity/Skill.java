package com.example.demo.entity;

public class Skill {
	
	private int id;
    private int userId;
    private String skill;

    public Skill(int id, int userId, String skill) {
    	this.id = id;
        this.userId = userId;
        this.skill = skill;
    }
    
    public int getId() {
        return id;
    }
    
    public int getUserId() {
        return userId;
    }

    public String getSkill() {
        return skill;
    }
}
