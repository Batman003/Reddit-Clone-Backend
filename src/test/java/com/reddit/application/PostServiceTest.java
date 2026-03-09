/**
 * 
 */
package com.reddit.application;

import java.time.Instant;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.reddit.application.dto.PostRequest;
import com.reddit.application.dto.PostResponse;
import com.reddit.application.mapper.PostMapper;
import com.reddit.application.model.Post;
import com.reddit.application.model.SubReddit;
import com.reddit.application.model.User;
import com.reddit.application.repository.PostRepository;
import com.reddit.application.repository.SubRedditRepository;
import com.reddit.application.repository.UserRepository;
import com.reddit.application.service.AuthService;
import com.reddit.application.service.PostService;

/**
 * @author batman007
 *
 */

@ExtendWith(MockitoExtension.class)
public class PostServiceTest {
	
	
	@Mock
	private PostRepository postRepository;
	@Mock
	private SubRedditRepository subreddit;
	@Mock
	private UserRepository userRepo;
	@Mock
	private AuthService authService;
	@Mock
	private PostMapper postMapper;
	
	@Captor
	private ArgumentCaptor<Post> postArgumentCaptor;
	
	Post post;
	PostService postService;
	User currentUser;
	SubReddit subRedditDTO;
	
	
	@BeforeEach
	void setUp() {
		 post = new Post(123L, "First Post", "http://url.site", "Test", null, null, Instant.now(), null);
		 postService = new PostService(subreddit, postRepository, userRepo, authService, postMapper);
		 currentUser = new User(123L, "test user", "password", "user@gmail.com", Instant.now(), true);
		 subRedditDTO = new SubReddit(123L, "First subreddit", "some description", null, Instant.now(), currentUser);
	}

	/**
	 * Test method for {@link com.reddit.application.service.PostService#save(com.reddit.application.dto.PostRequest)}.
	 */
	@Test
	@DisplayName("This test used for testing post creation bahaviour")
	void shouldSavePosts() {
		PostRequest postRequest = new PostRequest(null, "First subreddit", "First Post", "http://url.site", "any description");
		
		Mockito.when(subreddit.findByName("First subreddit")).thenReturn(Optional.of(subRedditDTO));
		Mockito.when(postMapper.map(postRequest, subRedditDTO, currentUser)).thenReturn(post);
		Mockito.when(authService.getCurrentUser()).thenReturn(currentUser);
		
		postService.save(postRequest);
		
		Mockito.verify(postRepository, Mockito.times(1)).save(postArgumentCaptor.capture());
		
		postArgumentCaptor.getValue();
		
		Assertions.assertThat(postArgumentCaptor.getValue().getPostId()).isEqualTo(123L);
		Assertions.assertThat(postArgumentCaptor.getValue().getPostName()).isEqualTo("First Post");
	}
	
	@Test
	@DisplayName("Should find post by id")
	void shouldFindByPostId() {
	
		PostResponse expePostResponse = new PostResponse(123L, "First Post", "http://url.site", "Test", "Test User", "Test Subreddit", 0, 0, "1 hour ago");
		Mockito.when(postRepository.findById(123L)).thenReturn(Optional.of(post));
		Mockito.when(postMapper.mapToDto(Mockito.any(Post.class))).thenReturn(expePostResponse);
		PostResponse actualPostResponse = postService.getPost(123L);
		Assertions.assertThat(actualPostResponse.getId()).isEqualTo(expePostResponse.getId());
		Assertions.assertThat(actualPostResponse.getPostName()).isEqualTo(expePostResponse.getPostName());
		
	}


	/**
	 * Test method for {@link com.reddit.application.service.PostService#getAllPost()}.
	 */
//	@Test
//	void testGetAllPost() {
//		fail("Not yet implemented");
//	}

	/**
	 * Test method for {@link com.reddit.application.service.PostService#getPostsBySubreddit(java.lang.Long)}.
	 */
//	@Test
//	void testGetPostsBySubreddit() {
//		fail("Not yet implemented");
//	}

	/**
	 * Test method for {@link com.reddit.application.service.PostService#getPostsByUsername(java.lang.String)}.
	 */
//	@Test
//	void testGetPostsByUsername() {
//		fail("Not yet implemented");
//	}
	
	
	/**
	 * Test method for {@link com.reddit.application.service.PostService#getPost(java.lang.Long)}.
	 */
	

}
