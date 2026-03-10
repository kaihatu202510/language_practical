package com.example.demo.controller;

import java.sql.SQLException;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.model.entity.User;
import com.example.demo.model.service.UserService;

@Controller
public class UserController {
	
	@Autowired
    private UserService userService;
	
	// ユーザー一覧
	@GetMapping("/")
	public String getUsers(
			Model model,
			@RequestParam(value = "keyword", required = false) String keyword,	
			@RequestHeader(value = "X-Requested-With", required = false) String	requestedWith	 
	) throws SQLException {

		List<User> userList = userService.getUsers(keyword);
		
		model.addAttribute("userList", userList);
		
		if ("XMLHttpRequest".equals(requestedWith)) { return
		"user/index :: userListFragment"; }			 

		return "user/index";
		
	}	
	
	// ユーザー作成画面
	@GetMapping("/user/new")
	public String newUser() {
		return "user/new";
	}

}