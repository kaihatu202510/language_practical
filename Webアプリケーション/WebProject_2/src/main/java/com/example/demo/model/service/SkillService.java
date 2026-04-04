package com.example.demo.model.service;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.example.demo.model.dao.SkillDao;
import com.example.demo.model.dao.UserDao;
import com.example.demo.model.dto.SkillDto;
import com.example.demo.model.dto.SkillView;
import com.example.demo.model.entity.Skill;
import com.example.demo.model.entity.User;
import com.example.demo.model.mapper.SkillMapper;
import com.example.demo.model.mapper.UserMapper;

@Service
public class SkillService {
	
    //private final SkillDao skillDao;
    private final UserService userService;
    private final SkillMapper skillMapper;
    private final UserMapper userMapper;

    @Autowired
    public SkillService(SkillDao skillDao, UserService userService, SkillMapper skillMapper, UserMapper userMapper) {
        // this.skillDao = skillDao;
        this.userService = userService;
        this.skillMapper = skillMapper;
        this.userMapper = userMapper;
    }

    // 全行
    public List<SkillView> getSkills() throws SQLException {
        return skillMapper.getSkills();
    }
    
    // IDで取得
    public Skill getSkill(int id) throws SQLException {
    	return skillMapper.getSkill(id);
    }

    // 作成
    public void createSkill(SkillDto request) throws SQLException {

        Integer userId = request.getUserId();
        String name = request.getName();

        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("スキル名を入力してください");
        }

        if (userService.getUser(userId) == null) {
            throw new IllegalArgumentException("存在しないユーザーです");
        }
        
        Skill skill = new Skill();
        skill.setUserId(userId);
        skill.setName(name);
        
        skillMapper.insertSkill(skill);
    }
    
    // 更新
    public void updateSkill(SkillDto dto) throws SQLException {
    	User loginUser =  userService.getLoginUser();        
    	Skill skill = skillMapper.getSkill(dto.getId());
		String name = dto.getName();
		
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("スキル名を入力してください");
        }
        
        skill.setName(name);        
		
     // ADMIN or 該当ユーザーならOK
        if (loginUser.getRole() != 1 &&
            skill.getUserId() != loginUser.getId()) {
            throw new AccessDeniedException("権限がありません");
        }

        skillMapper.updateSkill(skill);
    }

    // 削除
    public void deleteSkill(int id) throws SQLException {
    	skillMapper.deleteSkill(id);
    }    

}