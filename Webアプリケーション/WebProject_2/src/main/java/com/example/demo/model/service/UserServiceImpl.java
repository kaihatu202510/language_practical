package com.example.demo.model.service;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.demo.model.entity.User;
import com.example.demo.model.mapper.UserMapper;

@Service
public class UserServiceImpl implements UserDetailsService {

	@Autowired
	private UserMapper userMapper;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		User user = userMapper.findByName(username);
		if (user == null) {
			throw new UsernameNotFoundException("User not found");
		}
		
	    String role;
	    if (user.getRole() == 1) {
	        role = "ROLE_ADMIN";
	    } else {
	        role = "ROLE_PUBLIC";
	    }
	    
		return new org.springframework.security.core.userdetails.User(
				user.getName(),
				user.getPassword(),
				Collections.singleton(new SimpleGrantedAuthority(role)));
	}
	
	
}