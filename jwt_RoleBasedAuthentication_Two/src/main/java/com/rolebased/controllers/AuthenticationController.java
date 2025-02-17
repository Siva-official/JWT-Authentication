package com.rolebased.controllers;

import java.io.IOException;
import java.net.http.HttpRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.exc.StreamWriteException;
import com.fasterxml.jackson.databind.DatabindException;
import com.rolebased.model.AuthRequest;
import com.rolebased.model.RegisterAccount;
import com.rolebased.service.AuthService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/auth/api")
public class AuthenticationController {
	
	private final AuthService authService;
	
	public AuthenticationController(AuthService authService) {
		this.authService = authService;
	}
	
	@PostMapping("/signUp")
	public ResponseEntity<?> registerAccount(@RequestBody RegisterAccount accountDetails){
		return authService.resiterAccount(accountDetails);
	}

	
	@PostMapping("/login")
	public ResponseEntity<?> loginAccount(@RequestBody AuthRequest credentials){
		return authService.loginAccount(credentials);
	}
	
	@PostMapping("/refreshToken")
	public void refreshToken(HttpServletRequest request ,HttpServletResponse response ) throws StreamWriteException, DatabindException, IOException{
		 authService.refreshToken(request,response);
	}

}
