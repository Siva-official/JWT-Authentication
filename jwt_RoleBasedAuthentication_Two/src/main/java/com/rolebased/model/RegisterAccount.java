package com.rolebased.model;

import java.util.Set;

import org.springframework.stereotype.Component;

import com.rolebased.entity.Role;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Component
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RegisterAccount {
	
	private Long id;
	
	private String firstName;
	
	private String lastName;
	
	private String email;
	
	private String password;
	
	private Set<Role> roles ;
	

}
