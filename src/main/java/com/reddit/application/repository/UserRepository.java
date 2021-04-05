package com.reddit.application.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.reddit.application.model.User;
import com.reddit.application.model.VerificationToken;

public interface UserRepository extends JpaRepository<User, Long>{

	Optional<User> findByUserName(String username);

	boolean existsUserByUserName(String username);


}
