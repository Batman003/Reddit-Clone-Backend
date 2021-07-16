package com.reddit.application.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.reddit.application.dto.PostRequest;
import com.reddit.application.dto.PostResponse;
import com.reddit.application.exception.SpringRedditException;
import com.reddit.application.mapper.PostMapper;
import com.reddit.application.model.Post;
import com.reddit.application.model.SubReddit;
import com.reddit.application.model.User;
import com.reddit.application.repository.PostRepository;
import com.reddit.application.repository.SubRedditRepository;
import com.reddit.application.repository.UserRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class PostService {
	
	@Autowired
	SubRedditRepository subRedditRepository;
	@Autowired
	PostRepository postRepository;
	@Autowired
	UserRepository userRepository;
	@Autowired
	AuthService authService;
	
	@Autowired
	PostMapper postMapper;
	
	@Transactional
	public Post save(PostRequest postRequest) {
		// TODO Auto-generated method stub
		System.out.println("naaaaaaaaaaaaaaaaaame : "+postRequest.getSubRedditName());
		SubReddit subReddit =subRedditRepository.findByName(postRequest.getSubRedditName())
					.orElseThrow(() -> new SpringRedditException("Subreddit Not found with subreddit name"));
		User currentUser = authService.getCurrentUser();
		
		return postRepository.save(postMapper.map(postRequest, subReddit, currentUser));
	}
	
	@Transactional(readOnly = true)
	public PostResponse getPost(Long id) {
		// TODO Auto-generated method stub
		Post post = postRepository.findById(id)
				.orElseThrow(() -> new SpringRedditException("Post not found - "+id));
		return postMapper.mapToDto(post);
		
	}
	
	@Transactional(readOnly = true)
	public List<PostResponse> getAllPost() {
		// TODO Auto-generated method stub
		return postRepository.findAll()
				.stream()
				.map(postMapper::mapToDto)
				.collect(Collectors.toList());
		
	}
	
	@Transactional(readOnly = true)
	public List<PostResponse> getPostsBySubreddit(Long id) {
		// TODO Auto-generated method stub
		SubReddit subReddit = subRedditRepository.findById(id)
				.orElseThrow(() -> new SpringRedditException("Subreddit not found by id - "+id));
		List<Post> posts = postRepository.findAllBySubReddit(subReddit);
		return posts.stream().map(postMapper::mapToDto).collect(Collectors.toList());
	}
	
	@Transactional(readOnly = true)
	public List<PostResponse> getPostsByUsername(String name) {
		// TODO Auto-generated method stub
		User user = userRepository.findByUserName(name)
				.orElseThrow(() -> new SpringRedditException("User is not found by Username - "+name));
		List<Post> posts = postRepository.findAllByUser(user);
		return posts.stream().map(postMapper::mapToDto).collect(Collectors.toList());
	}

}
