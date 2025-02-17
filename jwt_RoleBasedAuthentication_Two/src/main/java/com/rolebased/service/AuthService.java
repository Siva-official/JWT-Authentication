package com.rolebased.service;

import java.io.IOException;

import org.aspectj.weaver.patterns.PerObject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.exc.StreamWriteException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rolebased.entity.Person;
import com.rolebased.model.AuthRequest;
import com.rolebased.model.AuthResponse;
import com.rolebased.model.RegisterAccount;
import com.rolebased.repository.AuthRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Service
public class AuthService {

	private final AuthRepository authRepository;
	
	private final JwtService jwtService;
	
	private final BCryptPasswordEncoder bCryptPasswordEncoder;
	
	private final AuthenticationManager authenticationManager;
	
	private final UserDetailsService userDetailsService;

	public AuthService(AuthRepository authRepository,JwtService jwtService,BCryptPasswordEncoder bCryptPasswordEncoder,
			AuthenticationManager authenticationManager,UserDetailsService userDetailsService) {
		this.authRepository = authRepository;
		this.jwtService = jwtService;
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
		this.authenticationManager= authenticationManager;
		this.userDetailsService=userDetailsService;
	}

	public ResponseEntity<?> resiterAccount(RegisterAccount accountDetails) {
		Person person = Person.builder().firstName(accountDetails.getFirstName()).lastName(accountDetails.getLastName())
				.password(bCryptPasswordEncoder.encode(accountDetails.getPassword())).email(accountDetails.getEmail())
				.roles(accountDetails.getRoles()).build();
		authRepository.save(person);
		
		String jwtToken= jwtService.generateToken(person);
		 String refreshToken = jwtService.refreshToken(person);
   		AuthResponse response = AuthResponse.builder().userName(person.getEmail()).refreshToken(refreshToken).jwtToken(jwtToken).build();
		return new ResponseEntity<>(response,HttpStatus.OK);
	}

	public ResponseEntity<?> loginAccount(AuthRequest credentials) {
		Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(credentials.getUserName(), credentials.getPassword()));
          if(authentication.isAuthenticated()) {
             Person person = Person.builder().email(credentials.getUserName()).password(credentials.getPassword()).build();
             String jwtToken= jwtService.generateToken(person);
             String refreshToken = jwtService.refreshToken(person);
      		AuthResponse response = AuthResponse.builder().userName(person.getEmail()).refreshToken(refreshToken).jwtToken(jwtToken).build();
      		return new ResponseEntity<>(response,HttpStatus.OK);
          }
		return new ResponseEntity<>("user not valid",HttpStatus.OK);
	}

	public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws StreamWriteException, DatabindException, IOException {


		final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
       
		if (authHeader == null || !authHeader.startsWith("Bearer")) {
			return;
		}
		 final String refreshToken = authHeader.substring(7);
		String userName = jwtService.extractUserName(refreshToken);
		if (userName != null ) {
			UserDetails userDetails = userDetailsService.loadUserByUsername(userName);
			if (jwtService.isTokenValid(refreshToken, userDetails)) {
				Person person = Person.builder().email(userDetails.getUsername()).password(userDetails.getPassword()).build();
			       String accessToken =	jwtService.generateToken(person);
			       AuthResponse authResponse = AuthResponse.builder()
			    		   .jwtToken(accessToken)
			    		   .refreshToken(refreshToken)
			    		   .userName(person.getEmail())
			    		   .build();
			       new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
			}
		}
	}

}
