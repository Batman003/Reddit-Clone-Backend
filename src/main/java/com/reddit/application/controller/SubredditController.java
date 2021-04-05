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

import com.reddit.application.dto.SubredditDTO;
import com.reddit.application.service.SubredditService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/subreddit")
@Slf4j
public class SubredditController {
	
	@Autowired
	SubredditService subredditService;
	
	@PostMapping
	public ResponseEntity<Object> createSubreddti(@RequestBody SubredditDTO subredditDTO) {
		log.info("In subreddit controller:: "+subredditDTO.toString());
		return new ResponseEntity<Object>(subredditService.save(subredditDTO),HttpStatus.CREATED);
	}
	
	@GetMapping
	public ResponseEntity<List<SubredditDTO>> getAllSubreddit(){
		log.info("In subreddit controller:: ");
		return new ResponseEntity<List<SubredditDTO>>(subredditService.getAllSubreddit(),HttpStatus.OK);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Object> getSubredditById(@PathVariable Long id){
		return new ResponseEntity<>(subredditService.getSubreddit(id),HttpStatus.OK);
	}
	
}
