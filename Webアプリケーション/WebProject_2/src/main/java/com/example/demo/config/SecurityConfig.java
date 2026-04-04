package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
	
	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
		// 認証設定
		.authorizeHttpRequests(auth -> auth	
			.requestMatchers("/", "/css/**").permitAll()
			.requestMatchers("/admin/**").hasRole("ADMIN")
			.anyRequest().authenticated()
			)
		
	    // admin以外が"/admin"にアクセスした際の遷移先
	    .exceptionHandling(ex -> ex
	        .accessDeniedPage("/access-denied")
	    )
		
		// ログイン設定
		.formLogin(form -> form
			.loginPage("/login")
			.permitAll()
			.defaultSuccessUrl("/users", true))
		
		// ログアウト設定
		.logout(logout -> logout
            .logoutUrl("/logout")
            .logoutSuccessUrl("/")
            .deleteCookies("JSESSIONID"));        
		
		return http.build();
	}
}