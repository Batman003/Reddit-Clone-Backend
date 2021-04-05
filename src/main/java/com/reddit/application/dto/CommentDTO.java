package com.reddit.application.dto;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentDTO {
	
	private Long id;
	private String text;
	private Long postId;
	private Instant createdDate;
	private String userName;
	
}
