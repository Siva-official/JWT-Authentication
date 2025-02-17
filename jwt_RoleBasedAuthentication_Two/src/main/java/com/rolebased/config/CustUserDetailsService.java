package com.rolebased.config;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.rolebased.entity.Person;
import com.rolebased.repository.AuthRepository;

@Component
public class CustUserDetailsService implements UserDetailsService {
	private final AuthRepository authRepository;

	public CustUserDetailsService(AuthRepository authRepository) {
		this.authRepository = authRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Person person =  authRepository.findByEmail(username).orElseThrow(()-> new UsernameNotFoundException("UserName not found exception"));
		return new CustUserDetails(person);
		
	}

}
