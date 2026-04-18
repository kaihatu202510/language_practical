package com.example.demo.model.entity;

public class Skill {
	
	private int id;
    private int userId;
    private String name;

    public Skill(int id, int userId, String name) {
    	this.id = id;
        this.userId = userId;
        this.name = name;
    }
    
    public Skill(int id, String name) {
    	this.id = id;
    	this.name = name;
    }
    
    public Skill() {};
    
    public int getId() {
        return id;
    }
    
    public int getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }
    
    public void setId(int id) {
    	this.id = id;
    }
    
    public void setUserId(int userId) {
    	this.userId = userId;
    }
    
    public void setName(String name) {
    	this.name = name;
    }
}
