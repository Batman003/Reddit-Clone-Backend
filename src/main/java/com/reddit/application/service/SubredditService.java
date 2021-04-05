package com.reddit.application.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.reddit.application.dto.SubredditDTO;
import com.reddit.application.exception.SpringRedditException;
import com.reddit.application.model.SubReddit;
import com.reddit.application.repository.SubRedditRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class SubredditService {
	
	@Autowired
	SubRedditRepository redditRepository;
	
	@Transactional
	public SubredditDTO save(SubredditDTO subredditDTO) {
		log.info("In subreddit service:: "+mapSubredditDto(subredditDTO).toString());
		SubReddit subReddit =  redditRepository.save(mapSubredditDto(subredditDTO));
		subredditDTO.setId(subReddit.getId());
		return subredditDTO;
	}
	
	@Transactional(readOnly = true)
	public List<SubredditDTO> getAllSubreddit() {
		return redditRepository.findAll()
			.stream()
			.map(this::mapToDto)
			.collect(Collectors.toList());
	}

	private SubredditDTO mapToDto(SubReddit subReddit) {
		// TODO Auto-generated method stub
		return SubredditDTO.builder().name(subReddit.getName())
				.id(subReddit.getId())
				.description(subReddit.getDescription())
				.numberOfPosts(subReddit.getPosts().size())
				.build();
	}

	private SubReddit mapSubredditDto(SubredditDTO subredditDTO) {
		// TODO Auto-generated method stub
		return SubReddit.builder()
				.name(subredditDTO.getName())
				.description(subredditDTO.getDescription())
				.build();
	}

	public SubredditDTO getSubreddit(Long id) {
		// TODO Auto-generated method stub
		SubReddit subReddit = redditRepository.findById(id)
				.orElseThrow(() -> new SpringRedditException("No subreddit found for given id!"));
		return mapToDto(subReddit);
	}
}
