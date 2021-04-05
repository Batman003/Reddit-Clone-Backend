package com.reddit.application.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.reddit.application.dto.VoteDTO;
import com.reddit.application.model.Vote;
import com.reddit.application.service.VoteService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/votes")
@AllArgsConstructor
public class VoteController {
	
	private final VoteService voteService;
	
	@PostMapping
	public ResponseEntity<Object> vote(@RequestBody VoteDTO voteDTO){
		Vote vote = voteService.vote(voteDTO);
		return new ResponseEntity<Object>(vote.getVoteId(),HttpStatus.CREATED);
		
	}
}
