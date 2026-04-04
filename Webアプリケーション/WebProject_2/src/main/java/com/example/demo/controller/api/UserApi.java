package com.example.demo.controller.api;

import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.dto.UserDto;
import com.example.demo.model.mapper.UserMapper;
import com.example.demo.model.service.UserService;

@RestController
public class UserApi {
	
	@Autowired
    private UserService userService;

	// ユーザー作成
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/api/users")
	public ResponseEntity<?> createtUser(@RequestBody UserDto request) throws SQLException {
		String name = request.getName();
		
	    if (name == null || name.trim().isEmpty()) {
	        return ResponseEntity
	                .badRequest()
	                .body("ユーザー名を入力してください");
	    }
	    
		userService.createUser(request);
		
		return ResponseEntity.ok().build();
	}

	// ユーザー削除
	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/api/users/{id}")
	public ResponseEntity<?> deleteUser(@PathVariable("id") int id) throws SQLException {
    	userService.deleteUser(id);
    	
    	return ResponseEntity.ok().build();
	}
}