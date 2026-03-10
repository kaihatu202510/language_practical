package com.example.demo.model.service;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.dao.SkillDao;
import com.example.demo.model.dto.SkillDto;
import com.example.demo.model.dto.SkillView;

@Service
public class SkillService {

    @Autowired
    private SkillDao skillDao;

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

        if (!skillDao.existsUser(userId)) {
            throw new IllegalArgumentException("存在しないユーザーです");
        }

        skillDao.insertSkill(userId, skill);
    }

    // 削除
    public void deleteSkill(int id) throws SQLException {

        int rows = skillDao.deleteSkill(id);

        if (rows == 0) {
            throw new IllegalArgumentException("存在しないIDです");
        }
    }
}