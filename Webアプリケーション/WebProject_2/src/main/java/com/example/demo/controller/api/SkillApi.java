package com.example.demo.controller.api;

import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.dto.SkillDto;
import com.example.demo.model.service.SkillService;

@RestController
public class SkillApi {

	@Autowired
	private SkillService skillService;

	// スキル作成
	@PostMapping("/api/skills")
	public ResponseEntity<?> createSkill(@RequestBody SkillDto request) throws SQLException {
	    try {
	        skillService.createSkill(request);
	        return ResponseEntity.ok().build();

	    } catch (IllegalArgumentException e) {
	        return ResponseEntity
	                .badRequest()
	                .body(e.getMessage());
	    }
	}
	
	// スキル削除
	@DeleteMapping("/api/skills/{id}")
	public ResponseEntity<?> deleteSkill(@PathVariable("id") int id) throws SQLException {
		skillService.deleteSkill(id);
		
	    return ResponseEntity.ok().build();
	}
}