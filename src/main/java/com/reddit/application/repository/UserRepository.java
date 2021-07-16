package com.reddit.application.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.reddit.application.model.User;
import com.reddit.application.model.VerificationToken;

public interface UserRepository extends JpaRepository<User, Long>{

	Optional<User> findByUserName(String username);

	boolean existsUserByUserName(String username);

	@Query(
		 value = "SELECT * FROM redd_users a WHERE a.user_name = ?1 AND a.enabled = 0",
		nativeQuery = true
		)
	Optional<User> findByUserNameandFlag(String username);


}
