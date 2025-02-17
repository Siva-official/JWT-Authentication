package com.rolebased.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rolebased.entity.Person;
import com.rolebased.repository.AuthRepository;

@RestController
@RequestMapping("/user/api")
//@PreAuthorize("hasRole('ADMIN')")
public class UserController {
	
	 @Autowired
	 AuthRepository authRepository;
	
	@GetMapping("/getAllUsers")
	@PreAuthorize("hasAnyRole('ADMIN', 'USER')")
	public List<Person> getAllUsers(){
		return authRepository.findAll();
	}

}
