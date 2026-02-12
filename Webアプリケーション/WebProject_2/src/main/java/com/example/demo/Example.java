package com.example.demo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.SkillRequest;
import com.example.demo.dto.SkillView;
import com.example.demo.dto.UserRequest;
import com.example.demo.entity.Skill;
import com.example.demo.entity.User;

import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;


@RestController
public class Example {
	@Autowired
	private DataSource dataSource;
	
	// ユーザー一覧
	@GetMapping("/api/users")
	public List<User> getUsers() throws SQLException {
		
		try (
			Connection connection = dataSource.getConnection();
			PreparedStatement statement = connection.prepareStatement("SELECT id, name FROM users");
			// sqlの実行結果（表）が入る
			ResultSet resultSet = statement.executeQuery();
		){
			List<User> users = new ArrayList<>();
			while (resultSet.next()) {
				users.add(
						new User(
								// 引数にはDBカラム名を指定
								resultSet.getInt("id"),
								resultSet.getString("name")
								)
						);
			}
			
			return users;
		}
	}
	
	// ユーザー作成
	@PostMapping("/api/users")
	public void createtUser(@RequestBody UserRequest request) throws SQLException {
		try(		
			Connection connection = dataSource.getConnection();
			PreparedStatement statement =
					connection.prepareStatement("INSERT INTO users (name) VALUES (?)");
		){
			String name = request.getName();
			statement.setString(1, name); //(?の位置, postされたname)
			statement.executeUpdate();
		}
		
	}
	
	// ユーザー削除
	@DeleteMapping("/api/users/{id}")
	public ResponseEntity<?> deleteUser(@PathVariable("id") int id) throws SQLException {
		try(
		    Connection connection = dataSource.getConnection();
		    PreparedStatement statement =
		        connection.prepareStatement("DELETE FROM users WHERE id = ?");
		){
		    statement.setInt(1, id);
	
		    int rows = statement.executeUpdate();
	
		    if (rows == 0) {
		        return ResponseEntity.badRequest().body("存在しないIDです");
		    }
	
		    return ResponseEntity.ok().build();
		}
	}
	
	
	
//	skill一覧取得
	@GetMapping("/api/skills")
	public List<SkillView> getSkills() throws SQLException {
		try(
			Connection connection = dataSource.getConnection();
			PreparedStatement statement = connection.prepareStatement(
					"SELECT s.id, u.name, s.skill " +
					"FROM skills AS s " +
					"JOIN users AS u ON s.user_id = u.id"
					);
			ResultSet resultSet = statement.executeQuery();
		){
			List<SkillView> skills = new ArrayList<>();
			while (resultSet.next()) {
				skills.add(
						new SkillView(
								resultSet.getInt("id"),
								resultSet.getString("name"),
								resultSet.getString("skill")
								)
						);
			}
			
			return skills;
		}
	}

	// スキル作成
	@PostMapping("/api/skills")
	public ResponseEntity<?> createSkill(@RequestBody SkillRequest request) throws SQLException {
		try(		
			Connection connection = dataSource.getConnection();			
			// userId 存在チェック
			PreparedStatement checkStmt =
				connection.prepareStatement("SELECT id FROM users WHERE id = ?");			
			
		){			
			Integer userId = request.getUserId();
			String skill = request.getSkill();
			
			checkStmt.setInt(1, userId);
			
			try(ResultSet rs = checkStmt.executeQuery()){				
				if (!rs.next()) {
					return ResponseEntity.badRequest()
						.body("指定された userId は存在しません");
				}
			}
			
			try(
				PreparedStatement statement =
						connection.prepareStatement("INSERT INTO skills (user_id, skill) VALUES (?, ?)");
				){
				statement.setInt(1, userId);
				statement.setString(2,  skill);
				statement.executeUpdate();	
			}	
	
			return ResponseEntity.ok().build();
		}
	}
	
	// スキル削除
	@DeleteMapping("/api/skills/{id}")
	public ResponseEntity<?> deleteSkill(@PathVariable("id") int id) throws SQLException {
		try(
		    Connection connection = dataSource.getConnection();
		    PreparedStatement statement =
		        connection.prepareStatement("DELETE FROM skills WHERE id = ?");
		){
		    statement.setInt(1, id);
	
		    int rows = statement.executeUpdate();
	
		    if (rows == 0) {
		        return ResponseEntity.badRequest().body("存在しないIDです");
		    }
	
		    return ResponseEntity.ok().build();
		}
	}
	
}