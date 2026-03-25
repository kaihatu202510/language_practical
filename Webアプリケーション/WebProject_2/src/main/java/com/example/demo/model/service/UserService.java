package com.example.demo.model.service;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.dao.UserDao;
import com.example.demo.model.dto.UserDto;
import com.example.demo.model.entity.User;

@Service
public class UserService {

    @Autowired
    private UserDao userDao;

    // 全行
    public List<User> getUsers(String keyword) throws SQLException {
        return userDao.findUsers(keyword);
    }

    // 単一
    public User getUser(int id) throws SQLException {
        return userDao.findUser(id);
    }

    // 作成
    public void createUser(UserDto request) throws SQLException {
        userDao.insertUser(request.getName());
    }

    // 削除
    public void deleteUser(int id) throws SQLException {

        int rows = userDao.deleteUser(id);

        if (rows == 0) {
            throw new IllegalArgumentException("存在しないIDです");
        }
    }
}