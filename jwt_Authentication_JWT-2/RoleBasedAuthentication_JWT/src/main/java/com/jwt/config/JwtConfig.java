package com.jwt.config;

import static org.springframework.security.config.Customizer.withDefaults;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.jwt.security.JwtAuthenticationFilter;
import com.jwt.security.JwtEntryPoint;

@Configuration
public class JwtConfig {

	private final UserDetailsService detailsService;

	private final JwtAuthenticationFilter authenticationFilter;
	
	private final JwtEntryPoint jwtEntryPoint;

	public JwtConfig(UserDetailsService service, JwtAuthenticationFilter authenticationFilter,JwtEntryPoint jwtEntryPoint) {
		this.detailsService = service;
		this.authenticationFilter = authenticationFilter;
		this.jwtEntryPoint= jwtEntryPoint;
	}

	@Bean
	SecurityFilterChain chain(HttpSecurity httpSecurity) throws Exception {
		return httpSecurity.csrf(csrf -> csrf.disable()).cors(cors -> cors.disable())
				.authorizeHttpRequests(
						authorized -> authorized.requestMatchers("/register","/login").permitAll().anyRequest().authenticated())
				.exceptionHandling(exception->exception.authenticationEntryPoint(jwtEntryPoint))
				.sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.authenticationProvider(authenticationProvider())
				.addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class)
				.build();
	}

//    @Bean
//    UserDetailsService detailsService() {
//	UserDetails user1=	User.builder().username("siva").password(passEncoder().encode("siva12")).roles("ADMIN").build();
//	UserDetails user2= User.builder().username("Ram").password(passEncoder().encode("ram12")).roles("USER").build();
//	return new InMemoryUserDetailsManager(user1,user2);
//	}

	@Bean
	PasswordEncoder passEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public AuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		provider.setUserDetailsService(detailsService);
		provider.setPasswordEncoder(passEncoder());
		return provider;
	}

	@Bean
	AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
		return configuration.getAuthenticationManager();
	}

}
