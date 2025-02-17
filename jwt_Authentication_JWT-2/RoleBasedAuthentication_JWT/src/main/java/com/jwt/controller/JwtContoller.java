package com.jwt.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.jwt.entity.User;
import com.jwt.service.JwtService;

@RestController
public class JwtContoller {

	@Autowired
	JwtService jwtService;

	@PostMapping("/register")
	public String regUser(@RequestBody User user) {
		return jwtService.regUserData(user);
	}

	@GetMapping("/get-user")
	public String logedinUsers(Principal principal) {
		return principal.getName();
	}

	@PostMapping("/login")
	public String loginUser(@RequestBody User user) {
        return jwtService.varifyUser(user);
	}

	
	@GetMapping("/getAll")
	public List<User> getAllUsers() {
		return jwtService.getALlUsers();
	}
}
