package com.example.demo.model.service;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.model.dao.UserDao;
import com.example.demo.model.dto.UserDto;
import com.example.demo.model.entity.User;
import com.example.demo.model.mapper.UserMapper;

@Service
public class UserService {

    // @Autowired
    // private UserDao userDao;
    
    @Autowired
    private UserMapper userMapper;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    // 全行
    public List<User> getUsers(String keyword) throws SQLException {
        return userMapper.getUsers(keyword);
    }

    // 単一
    public User getUser(int id) throws SQLException {
        return userMapper.findUserById(id);
    }

    // 作成
    public void createUser(UserDto dto) throws SQLException {
    	User user = new User();
    	user.setName(dto.getName());
    	user.setPassword(passwordEncoder.encode(dto.getPassword()));
    	user.setEnabled(true);
    	user.setRole(dto.getRole());
    	
        userMapper.insertUser(user);
    }

    // 削除
    public void deleteUser(int id) throws SQLException {

        int rows = userMapper.deleteUser(id);

        if (rows == 0) {
            throw new IllegalArgumentException("存在しないIDです");
        }
    }
    
    // ログインユーザー取得
    public User getLoginUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userName = auth.getName();

        User loginUser = userMapper.findByName(userName);
        
        return loginUser;
    }
}