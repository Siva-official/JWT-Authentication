package com.rolebased.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rolebased.entity.Person;

@Repository
public interface AuthRepository extends JpaRepository<Person, Long>{

	Optional<Person> findByEmail(String username);

}
