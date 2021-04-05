package com.reddit.application.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.reddit.application.dto.PostRequest;
import com.reddit.application.dto.PostResponse;
import com.reddit.application.service.PostService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/posts")
@Slf4j
public class PostController {
	
	@Autowired
	PostService postService;
	
	@PostMapping
	public ResponseEntity<Object> createPost(@RequestBody PostRequest postRequest){
		return new ResponseEntity<Object>(postService.save(postRequest),HttpStatus.CREATED);
		
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<PostResponse> getPost(@PathVariable Long id){
		return new ResponseEntity<PostResponse>(postService.getPost(id),HttpStatus.OK);
	}
	
	
	@GetMapping
	public ResponseEntity<List<PostResponse>> getAllPost(){
		List<PostResponse> posts = postService.getAllPost();
		log.info("All Posts :: "+posts);
		return new ResponseEntity<List<PostResponse>>(posts,HttpStatus.OK);
	}
	
	@GetMapping("/by-subreddit/{id}")
	public ResponseEntity<List<PostResponse>> getPostBySubreddit(@PathVariable Long id){
		List<PostResponse> posts = postService.getPostsBySubreddit(id);
		return new ResponseEntity<List<PostResponse>>(posts,HttpStatus.OK);
	}
	
	@GetMapping("/by-user/{name}")
	public ResponseEntity<List<PostResponse>> getPostByUsername(@PathVariable String name){
		List<PostResponse> posts = postService.getPostsByUsername(name);
		return new ResponseEntity<List<PostResponse>>(posts,HttpStatus.OK);
	}

}
