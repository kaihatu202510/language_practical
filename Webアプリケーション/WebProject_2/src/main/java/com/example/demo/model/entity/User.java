package com.example.demo.model.entity;

public class User {

    private int id;
    private String name;
    private String password;
    private boolean enabled;
    private int role;

    public User(int id, String name, String password, boolean enabled, int role) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.enabled = enabled;
    }
    
    public User(int id, String name) {
        this.id = id;
        this.name = name;
    }
    
    public User() {};
    

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
    
    public String getPassword() {
    	return password;
    }
    
    public boolean getEnabled() {
    	return enabled;
    }
    
    public Integer getRole() {
    	return role;
    }
    
    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
    
    public void setRole(int role) {
    	this.role = role;
    }
    
}
