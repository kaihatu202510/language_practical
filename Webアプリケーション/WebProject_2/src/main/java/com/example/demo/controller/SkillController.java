package com.example.demo.controller;

import java.sql.Connection;
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
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.dto.SkillRequest;
import com.example.demo.dto.SkillView;
import com.example.demo.dto.UserRequest;
import com.example.demo.entity.Skill;
import com.example.demo.entity.User;

import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;


@Controller
public class SkillController {
	@Autowired
	private DataSource dataSource;
		
	//skill一覧取得
	@GetMapping("/skills")
	public String getSkills(
	        Model model,
	        @RequestHeader(value = "X-Requested-With", required = false) String requestedWith
	) throws SQLException {

	    StringBuilder sql = new StringBuilder(
	        "SELECT s.id, u.name, s.skill " +
	        "FROM skills s " +
	        "JOIN users u ON s.user_id = u.id"
	    );

	    try (
	        Connection connection = dataSource.getConnection();
	        PreparedStatement statement = connection.prepareStatement(sql.toString())
	    ) {
	        try (ResultSet rs = statement.executeQuery()) {

	            List<SkillView> skills = new ArrayList<>();

	            while (rs.next()) {
	                skills.add(new SkillView(
	                        rs.getInt("id"),
	                        rs.getString("name"),
	                        rs.getString("skill")
	                ));
	            }

	            model.addAttribute("skillList", skills);
	        }

	        if ("XMLHttpRequest".equals(requestedWith)) {
	            return "skill/index :: skillListFragment";
	        }

	        return "skill/index";
	    }
	}
	
	//スキル作成画面
	@GetMapping("/users/{id}/skills/new")
	public String newSkill(@PathVariable("id") int id, Model model) throws SQLException {
		try(
				Connection connection = dataSource.getConnection();			
				PreparedStatement checkStmt =
					connection.prepareStatement("SELECT id, name FROM users WHERE id = ?");
		){			
			checkStmt.setInt(1, id);
			ResultSet rs = checkStmt.executeQuery();
			rs.next();			
			
			User user = new User(
					rs.getInt("id"),
					rs.getString("name")
					);
			model.addAttribute("user", user);
		}
		
		return "skill/new";
	}
	

	// スキル作成
	@PostMapping("/api/skills")
	public ResponseEntity<?> createSkill(@RequestBody SkillRequest request) throws SQLException {
		
		Integer userId = request.getUserId();
		String skill = request.getSkill();
		
	    if (skill == null || skill.trim().isEmpty()) {
	        return ResponseEntity
	                .badRequest()
	                .body("スキル名を入力してください");
	    }
	    
		try(
				Connection connection = dataSource.getConnection();			
				// userId 存在チェック
				PreparedStatement checkStmt =
					connection.prepareStatement("SELECT id FROM users WHERE id = ?");			
			
		){
			checkStmt.setInt(1, userId);
			ResultSet rs = checkStmt.executeQuery();
			if (!rs.next()) {
			    return ResponseEntity
			            .badRequest()
			            .body("存在しないユーザーです");
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