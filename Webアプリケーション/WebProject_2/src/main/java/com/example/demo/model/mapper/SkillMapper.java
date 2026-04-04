package com.example.demo.model.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import com.example.demo.model.dto.SkillView;
import com.example.demo.model.entity.Skill;

@Mapper
public interface SkillMapper {
    public List<SkillView> getSkills();
    public Skill getSkill(int id);
    public void insertSkill(Skill skill);
    public void deleteSkill(int id);
    public void updateSkill(Skill skill);
}