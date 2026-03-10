package com.example.demo.model.dao;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.example.demo.model.entity.User;

@Repository
public class UserDao {

    @Autowired
    private DataSource dataSource;

    // ユーザー一覧取得
    public List<User> findUsers(String keyword) throws SQLException {

        String sql = "SELECT id, name FROM users";
        boolean hasKeyword = keyword != null && !keyword.isBlank();

        if (hasKeyword) {
            sql += " WHERE name LIKE ?";
        }

        try (
            Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {

            if (hasKeyword) {
                statement.setString(1, "%" + keyword + "%");
            }

            try (ResultSet rs = statement.executeQuery()) {

                List<User> userList = new ArrayList<>();

                while (rs.next()) {
                    userList.add(new User(
                            rs.getInt("id"),
                            rs.getString("name")
                    ));
                }

                return userList;
            }
        }
    }

    // ユーザー取得
    public User findUser(int id) throws SQLException {

        try (
            Connection connection = dataSource.getConnection();
            PreparedStatement statement =
                    connection.prepareStatement("SELECT id, name FROM users WHERE id = ?")
        ) {

            statement.setInt(1, id);

            ResultSet rs = statement.executeQuery();

            if (!rs.next()) {
                return null;
            }

            return new User(
                    rs.getInt("id"),
                    rs.getString("name")
            );
        }
    }

    // 作成
    public void insertUser(String name) throws SQLException {

        try (
            Connection connection = dataSource.getConnection();
            PreparedStatement statement =
                    connection.prepareStatement("INSERT INTO users (name) VALUES (?)")
        ) {

            statement.setString(1, name);
            statement.executeUpdate();
        }
    }

    // 削除
    public int deleteUser(int id) throws SQLException {

        try (
            Connection connection = dataSource.getConnection();
            PreparedStatement statement =
                    connection.prepareStatement("DELETE FROM users WHERE id = ?")
        ) {

            statement.setInt(1, id);

            return statement.executeUpdate();
        }
    }
}