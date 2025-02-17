package com.jwt.config;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.jwt.entity.User;
import com.jwt.repository.JwtRepository;

@Component
public class CustUserDetailsService implements UserDetailsService {
	
	private final JwtRepository jwtRepository;
	
	public CustUserDetailsService(JwtRepository jwtRepositor) {
		this.jwtRepository = jwtRepositor;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user= jwtRepository.findByFullName(username);
         if(user == null) {
        	 throw new UsernameNotFoundException("User not found ..!!!");
         }
         return new CustUserDetails(user);
	}

}
