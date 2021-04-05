package com.reddit.application.model;

import java.util.Arrays;

import com.reddit.application.exception.SpringRedditException;

public enum VoteType {
	
	UPVOTE(1),
	DOWNVOTE(-1),
	
;
	private Integer direction;
	
	 VoteType(Integer direction) {
		// TODO Auto-generated constructor stub
	}
	 
	 private Integer getDirection() {
		 return direction;
	 }
	 
	 public static VoteType lookup(Integer direction) {
		 return Arrays.stream(VoteType.values())
				 .filter(value -> value.getDirection().equals(direction))
				 .findAny()
				 .orElseThrow(() -> new SpringRedditException("Vote not found"));
	 }

}
