package com.example.demo.model.service;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.dao.SkillDao;
import com.example.demo.model.dao.UserDao;
import com.example.demo.model.dto.SkillDto;
import com.example.demo.model.dto.SkillView;

@Service
public class SkillService {
	
    private final SkillDao skillDao;
    private final UserService userService;

    @Autowired
    public SkillService(SkillDao skillDao, UserService userService) {
        this.skillDao = skillDao;
        this.userService = userService;
    }

    // 全行
    public List<SkillView> getSkills() throws SQLException {
        return skillDao.findSkills();
    }

    // 作成
    public void createSkill(SkillDto request) throws SQLException {

        Integer userId = request.getUserId();
        String skill = request.getSkill();

        if (skill == null || skill.trim().isEmpty()) {
            throw new IllegalArgumentException("スキル名を入力してください");
        }

        if (userService.getUser(userId) == null) {
            throw new IllegalArgumentException("存在しないユーザーです");
        }

        skillDao.insertSkill(userId, skill);
    }

    // 削除
    public void deleteSkill(int id) throws SQLException {

        int rows = skillDao.deleteSkill(id);

    }
}