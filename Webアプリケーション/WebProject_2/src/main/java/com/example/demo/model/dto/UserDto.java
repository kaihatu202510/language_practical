package com.example.demo.model.dto;

public class UserDto {
	private Integer id;
	private String name;
	private String password;
	private Integer role;
	
    public UserDto() {}
    
	public Integer getid() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	public String getPassword() {
		return password;
	}
	
	public Integer getRole() {
		return role;
	}
	
	public void setid(Integer id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public void setRole(Integer role) {
		this.role = role;
	}
}
