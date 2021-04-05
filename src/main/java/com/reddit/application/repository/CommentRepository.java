package com.reddit.application.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.reddit.application.model.Comment;
import com.reddit.application.model.Post;
import com.reddit.application.model.User;

public interface CommentRepository extends JpaRepository<Comment, Long>{

	List<Comment> findByPost(Post post);

	List<Comment> findByUser(User user);

}
