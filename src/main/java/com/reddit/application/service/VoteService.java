package com.reddit.application.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.reddit.application.dto.VoteDTO;
import com.reddit.application.exception.SpringRedditException;
import com.reddit.application.model.Post;
import com.reddit.application.model.Vote;
import com.reddit.application.model.VoteType;
import com.reddit.application.repository.PostRepository;
import com.reddit.application.repository.VoteRepository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@AllArgsConstructor
@Slf4j
public class VoteService {
	
	private final VoteRepository voteRepository;
	private final PostRepository postRepository;
	private final AuthService authService;
	
	
	public Vote vote(VoteDTO voteDTO) {
		// TODO Auto-generated method stub
		
		Post post = postRepository.findById(voteDTO.getPostId())
				.orElseThrow(() -> new SpringRedditException("Post not found against postId - "+voteDTO.getPostId()));
		Optional<Vote> voteByPostAndUser = voteRepository.findTopByPostAndUserOrderByVoteIdDesc(post, authService.getCurrentUser());
		
		if(voteByPostAndUser.isPresent() && voteByPostAndUser.get().getVoteType().equals(voteDTO.getVoteType())) {
			throw new SpringRedditException("You have already " + voteDTO.getVoteType() + "'d for this post");
		}
		
		if(VoteType.UPVOTE.equals(voteDTO.getVoteType())) {
			post.setVoteCount(post.getVoteCount()+1);
		}else {
			post.setVoteCount(post.getVoteCount()-1);
		}
		
		voteRepository.save(mapToDTO(voteDTO, post));
		postRepository.save(post);
		return mapToDTO(voteDTO, post);
	}


	private Vote mapToDTO(VoteDTO voteDTO, Post post) {
		// TODO Auto-generated method stub
		return Vote.builder()
				.voteType(voteDTO.getVoteType())
				.post(post)
				.user(authService.getCurrentUser())
				.build();
	}

}
