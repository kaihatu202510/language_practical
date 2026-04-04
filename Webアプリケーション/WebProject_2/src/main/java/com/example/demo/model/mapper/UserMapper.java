package com.example.demo.model.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.example.demo.model.entity.User;

@Mapper
public interface UserMapper {
	User findByName(String name);
	User findUserById(int id);
	List<User> getUsers(String keyword);
	void insertUser(User user);
	int deleteUser(int id);
}