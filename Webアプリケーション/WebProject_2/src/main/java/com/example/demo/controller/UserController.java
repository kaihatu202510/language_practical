package com.example.demo.controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.dto.UserRequest;
import com.example.demo.entity.User;

@Controller
public class UserController {
	@Autowired
	private DataSource dataSource;
	
	// ユーザー一覧
	@GetMapping("/")
	public String getUsers(
			Model model,
			@RequestParam(value = "keyword", required = false) String keyword,	
			@RequestHeader(value = "X-Requested-With", required = false) String	requestedWith	 
	) throws SQLException {

		String sql = "SELECT id, name FROM users";
		boolean hasKeyword = keyword != null && !keyword.isBlank();

		if (hasKeyword) {
			sql += " WHERE name LIKE ?";
		}

		try (Connection connection = dataSource.getConnection();
				PreparedStatement statement = connection.prepareStatement(sql);) {

			if (hasKeyword) {
				statement.setString(1, "%" + keyword + "%");
			}

			try (ResultSet resultSet = statement.executeQuery()) {

				List<User> userList = new ArrayList<>();

				while (resultSet.next()) {
					userList.add(new User(resultSet.getInt("id"), resultSet.getString("name")));
				}

				model.addAttribute("userList", userList);
			}
			
			if ("XMLHttpRequest".equals(requestedWith)) { return
			"user/index :: userListFragment"; }			 

			return "user/index";
		}
	}

	

	// ユーザー単体取得 いらない
	@GetMapping("/users/{id}")
	public String showUser(
			@PathVariable("id") int id,
			Model model
		) throws SQLException {

		try (Connection connection = dataSource.getConnection();
				PreparedStatement statement = connection.prepareStatement("SELECT id, name FROM users WHERE id = ?");) {

			statement.setInt(1, id);

			try (ResultSet resultSet = statement.executeQuery()) {
				resultSet.next();
				
				User user = new User(
						resultSet.getInt("id"),
						resultSet.getString("name")
						);
				model.addAttribute("user", user);
				
				return "skill/new";
			}
		}
	}
	
	
	// ユーザー作成画面
	@GetMapping("/user/new")
	public String newUser() {
		return "user/new";
	}
	
	
	// ユーザー作成
	@PostMapping("/api/users")
	public ResponseEntity<?> createtUser(@RequestBody UserRequest request) throws SQLException {
		try (Connection connection = dataSource.getConnection();
				PreparedStatement statement = connection.prepareStatement("INSERT INTO users (name) VALUES (?)");) {
			String name = request.getName();
			
		    if (name == null || name.trim().isEmpty()) {
		        return ResponseEntity
		                .badRequest()
		                .body("ユーザー名を入力してください");
		    }
		    
			statement.setString(1, name); // (?の位置, postされたname)
			statement.executeUpdate();
		}
		
		return ResponseEntity.ok().build();
	}

	// ユーザー削除
	@DeleteMapping("/api/users/{id}")
	public ResponseEntity<?> deleteUser(@PathVariable("id") int id) throws SQLException {
		try (Connection connection = dataSource.getConnection();
				PreparedStatement statement = connection.prepareStatement("DELETE FROM users WHERE id = ?");) {
			statement.setInt(1, id);

			int rows = statement.executeUpdate();

			if (rows == 0) {
				return ResponseEntity.badRequest().body("存在しないIDです");
			}

			return ResponseEntity.ok().build();
		}
	}

}