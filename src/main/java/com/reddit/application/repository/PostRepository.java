package com.reddit.application.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.reddit.application.model.Post;
import com.reddit.application.model.SubReddit;
import com.reddit.application.model.User;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

	List<Post> findAllBySubReddit(SubReddit subReddit);

	List<Post> findAllByUser(User user);

}
