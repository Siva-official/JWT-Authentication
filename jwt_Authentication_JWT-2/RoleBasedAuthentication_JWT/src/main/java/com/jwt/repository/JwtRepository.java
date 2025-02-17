package com.jwt.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jwt.entity.User;

@Repository
public interface JwtRepository extends JpaRepository<User, Long>{
	 Optional<User> findByEmail(String email);

	User findByFullName(String username);
}
