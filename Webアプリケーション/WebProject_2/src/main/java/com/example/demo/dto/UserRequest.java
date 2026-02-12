package com.example.demo.dto;

public class UserRequest {
	private Integer id;
	private String name;
	
    public UserRequest() {}
    
	public Integer getid() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setid(Integer id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}
}
