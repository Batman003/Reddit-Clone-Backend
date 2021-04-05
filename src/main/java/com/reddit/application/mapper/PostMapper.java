package com.reddit.application.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.marlonlom.utilities.timeago.TimeAgo;
import com.reddit.application.dto.PostRequest;
import com.reddit.application.dto.PostResponse;
import com.reddit.application.model.Post;
import com.reddit.application.model.SubReddit;
import com.reddit.application.model.User;
import com.reddit.application.repository.CommentRepository;
import com.reddit.application.repository.VoteRepository;
import com.reddit.application.service.AuthService;


@Mapper(componentModel = "spring")
public abstract class PostMapper {
	
	@Autowired
	private CommentRepository commentRepo;
	
	@Autowired
	private VoteRepository voteRepo;
	
	@Autowired
	private AuthService authService;
	 
	
	@Mapping(target = "createDate", expression = "java(java.time.Instant.now())")
	@Mapping(target = "subReddit", source = "subReddit")
	@Mapping(target = "user", source = "user")
	@Mapping(target = "description", source = "postRequest.description")
	@Mapping(target = "voteCount", constant = "0")
	
	public abstract Post map(PostRequest postRequest, SubReddit subReddit, User user);
	
	@Mapping(target = "id", source = "postId")
	@Mapping(target = "postName", source = "postName")
	@Mapping(target = "description", source = "description")
	@Mapping(target = "url", source = "url")
	@Mapping(target = "subredditName", source = "subReddit.name")
	@Mapping(target = "username", source = "user.userName")
	@Mapping(target = "commentCount", expression = "java(commentCount(post))")
	@Mapping(target = "duration", expression = "java(getDuration(post))")
	
	public abstract PostResponse mapToDto(Post post);
	
	Integer commentCount(Post post) {return commentRepo.findByPost(post).size();}
	String getDuration(Post post) {return TimeAgo.using(post.getCreateDate().toEpochMilli());}
}
