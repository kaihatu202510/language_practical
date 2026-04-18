package com.example.demo.controller;

import java.sql.SQLException;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import com.example.demo.model.dto.SkillView;
import com.example.demo.model.entity.Skill;
import com.example.demo.model.entity.User;
import com.example.demo.model.service.SkillService;
import com.example.demo.model.service.UserService;
import com.example.demo.model.service.UserServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;


@Controller
public class SkillController {

	private SkillService skillService;
	private UserService userService;
	
	@Autowired
	public SkillController(SkillService skillService, UserService userService, UserServiceImpl userServiceImpl) {
		this.skillService = skillService;
		this.userService = userService;	
	}
	
		
	//skill一覧取得
	@GetMapping("/skills")
	public String getSkills(
	        Model model,
	        @RequestHeader(value = "X-Requested-With", required = false) String requestedWith
	) throws SQLException {
		
		List<SkillView> skills = skillService.getSkills();
		User loginUser = userService.getLoginUser();
		
		model.addAttribute("loginUser", loginUser);		
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
		User loginUser = userService.getLoginUser();
		
		if(loginUser.getRole() == 1 || (user != null && user.getId() == loginUser.getId())) {
			model.addAttribute("user", user);			
			return "skill/new";
		} else {
			return "error/accessDenied";
		}
		
	}
	
	//スキル編集画面
	@GetMapping("/skill/edit/{id}")
	public String editSkill(@PathVariable("id") int id, Model model) throws SQLException {		
		Skill skill = skillService.getSkill(id);
		User loginUser = userService.getLoginUser();
		Integer userId = skill.getUserId();
		
		if(loginUser.getRole() == 1 || (userId != null && userId == loginUser.getId())) {
			model.addAttribute("skill", skill);		
			return "skill/edit";
		} else {
			return "error/accessDenied";
		}
		
	}
	
	
}