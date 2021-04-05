package com.reddit.application.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.reddit.application.dto.CommentDTO;
import com.reddit.application.model.Comment;
import com.reddit.application.model.Post;
import com.reddit.application.model.User;

@Mapper(componentModel = "spring")
public interface CommentMapper {
	
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "text", source = "commentDTO.text")
	@Mapping(target = "createDate", expression = "java(java.time.Instant.now())")
	@Mapping(target = "post", source = "post")
	@Mapping(target = "user", source="user")
	
	Comment map(CommentDTO commentDTO, Post post, User user);
	
	@Mapping(target = "text", source = "text")
	@Mapping(target = "postId", source = "comment.post.postId")
	@Mapping(target = "userName", source = "comment.user.userName")
	@Mapping(target = "createdDate", source = "comment.createDate")
	
	CommentDTO mapToDto(Comment comment);
}
