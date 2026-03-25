package com.example.demo.controller;

import java.sql.SQLException;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import com.example.demo.model.dto.SkillView;
import com.example.demo.model.entity.User;
import com.example.demo.model.service.SkillService;
import com.example.demo.model.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;


@Controller
public class SkillController {
	@Autowired
	private SkillService skillService;
	@Autowired
	private UserService userService;
		
	//skill一覧取得
	@GetMapping("/skills")
	public String getSkills(
	        Model model,
	        @RequestHeader(value = "X-Requested-With", required = false) String requestedWith
	) throws SQLException {
		
		List<SkillView> skills = skillService.getSkills();
		
        model.addAttribute("skillList", skills);        

        if ("XMLHttpRequest".equals(requestedWith)) {
            return "skill/index :: skillListFragment";
        }

        return "skill/index";
    }
	
	
	//スキル作成画面
	@GetMapping("/users/{id}/skills/new")
	public String newSkill(@PathVariable("id") int id, Model model) throws SQLException {		
		User user = userService.getUser(id);
		
		model.addAttribute("user", user);
		
		return "skill/new";
	}
	
	
}