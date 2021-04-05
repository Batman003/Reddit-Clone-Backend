package com.reddit.application.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import com.reddit.application.constant.RedditConstant;
import com.reddit.application.dto.CommentDTO;
import com.reddit.application.exception.SpringRedditException;
import com.reddit.application.mapper.CommentMapper;
import com.reddit.application.model.Comment;
import com.reddit.application.model.NotificationEmail;
import com.reddit.application.model.Post;
import com.reddit.application.model.User;
import com.reddit.application.repository.CommentRepository;
import com.reddit.application.repository.PostRepository;
import com.reddit.application.repository.UserRepository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@AllArgsConstructor
@Slf4j
public class CommentsService {

	private final CommentRepository commentRepository;
	private final PostRepository postRepository;
	private final CommentMapper commentMapper;
	private final UserRepository userRepository;
	private final AuthService authService;
	private final MailContentBuilder contentBuilder;
	private final MailService mailService;
	private final MessageSource messageSource;

	public Comment save(CommentDTO commentDTO) {
		// TODO Auto-generated method stub
		Post post = postRepository.findById(commentDTO.getPostId()).orElseThrow(
				() -> new SpringRedditException("Post is not found by postId - " + commentDTO.getPostId()));
		Comment comment = commentMapper.map(commentDTO, post, authService.getCurrentUser());	
		log.info("comment entity created before storing database:: "+comment);
		comment = commentRepository.save(comment);
		String message = contentBuilder.build(messageSource.getMessage("reddit.comment.email.body",
				new Object[] { comment.getUser().getUserName(), comment.getPost().getUrl() }, null));
		sendCommentNotification(message, post.getUser());
		return comment;
	}

	private void sendCommentNotification(String message, User user) {
		// TODO Auto-generated method stub
		mailService.sendMail(new NotificationEmail(
				messageSource.getMessage("redit.comment.email.sub", new Object[] { user.getUserName() }, null),
				user.getEmail(), message));
	}

	public List<CommentDTO> getAllCommentsForPost(Long postId) {
		// TODO Auto-generated method stub
		Post post = postRepository.findById(postId)
				.orElseThrow(() -> new SpringRedditException("Post is not found against postId:: "+postId));
		return commentRepository.findByPost(post).stream()
				.map(commentMapper::mapToDto)
				.collect(Collectors.toList());
	}

	public List<CommentDTO> getAllCommentsForPost(String userName) {
		// TODO Auto-generated method stub
		User user = userRepository.findByUserName(userName)
				.orElseThrow(() -> new SpringRedditException("User not found against username :: "+userName));
		return commentRepository.findByUser(user).stream()
				.map(commentMapper::mapToDto)
				.collect(Collectors.toList());
	}

}
