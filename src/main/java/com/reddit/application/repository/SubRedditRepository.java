package com.reddit.application.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.reddit.application.model.SubReddit;
import com.reddit.application.model.User;

public interface SubRedditRepository extends JpaRepository<SubReddit, Long>{

	Optional<SubReddit> findByName(String subRedditName);

}
