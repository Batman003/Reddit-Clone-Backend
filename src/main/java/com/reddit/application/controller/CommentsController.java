package com.reddit.application.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.reddit.application.dto.CommentDTO;
import com.reddit.application.model.Comment;
import com.reddit.application.service.CommentsService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/comments")
@AllArgsConstructor
@Slf4j
public class CommentsController {
	
	private final CommentsService commentsService;
	
	
	@PostMapping
	public ResponseEntity<Object> createComment(@RequestBody CommentDTO commentDTO){
		Comment comment = commentsService.save(commentDTO);
		log.info("Comment created succesfully:: "+comment);
		return new ResponseEntity<>(comment.getId(),HttpStatus.CREATED);
		
	}
	
	@GetMapping("/by-post/{postId}")
	public ResponseEntity<List<CommentDTO>> getAllCommentsForPost(@PathVariable Long postId){
		List<CommentDTO> comments = commentsService.getAllCommentsForPost(postId);
		return new ResponseEntity<>(comments,HttpStatus.OK);
	}
	
	@GetMapping("/by-user/{userName}")
	public ResponseEntity<List<CommentDTO>> getAllCommentsForUser(@PathVariable String userName){
		List<CommentDTO> comments = commentsService.getAllCommentsForPost(userName);
		return new ResponseEntity<>(comments,HttpStatus.OK);
	}
}
