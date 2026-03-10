package com.example.demo.model.dao;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.example.demo.model.dto.SkillView;

@Repository
public class SkillDao {

    @Autowired
    private DataSource dataSource;

    // スキル一覧
    public List<SkillView> findSkills() throws SQLException {

        String sql =
            "SELECT s.id, u.name, s.skill " +
            "FROM skills s " +
            "JOIN users u ON s.user_id = u.id";

        try (
            Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet rs = statement.executeQuery()
        ) {

            List<SkillView> skills = new ArrayList<>();

            while (rs.next()) {
                skills.add(new SkillView(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("skill")
                ));
            }

            return skills;
        }
    }

    // ユーザー存在確認
    public boolean existsUser(int userId) throws SQLException {

        String sql = "SELECT id FROM users WHERE id = ?";

        try (
            Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {

            statement.setInt(1, userId);

            ResultSet rs = statement.executeQuery();

            return rs.next();
        }
    }

    // スキル作成
    public void insertSkill(int userId, String skill) throws SQLException {

        String sql = "INSERT INTO skills (user_id, skill) VALUES (?, ?)";

        try (
            Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {

            statement.setInt(1, userId);
            statement.setString(2, skill);

            statement.executeUpdate();
        }
    }

    // 削除
    public int deleteSkill(int id) throws SQLException {

        String sql = "DELETE FROM skills WHERE id = ?";

        try (
            Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {

            statement.setInt(1, id);

            return statement.executeUpdate();
        }
    }
}