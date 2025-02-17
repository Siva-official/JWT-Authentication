package com.jwt.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.jwt.entity.User;
import com.jwt.repository.JwtRepository;
import com.jwt.security.JwtHelper;

@Service
public class JwtService {


	@Autowired
	JwtRepository jwtRepository;
	
	@Autowired
	PasswordEncoder passEncoder;
	
	@Autowired
	JwtHelper helper;
	
	
    private final	AuthenticationManager authenticationManager;
    
    public JwtService(AuthenticationManager authenticationManager) {
    	this.authenticationManager = authenticationManager;
    }

	
	public String regUserData(User user) {
		user.setPassword(passEncoder.encode(user.getPassword()));
		jwtRepository.save(user);
		return "User Registraation Success...!!!!";
	}

	public  String varifyUser(User user) {
		Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getFullName(), user.getPassword()));
		if(authentication.isAuthenticated()) {
			return helper.generateToken(user);
		}
		return "failure";
	}


	public List<User> getALlUsers() {
		return jwtRepository.findAll();
	}


}
