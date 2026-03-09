/**
 * 
 */
package com.reddit.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.reddit.application.exception.SpringRedditException;
import com.reddit.application.service.CommentsService;

/**
 * @author batman007
 *
 */
public class CommentServiceTest {

	/**
	 * Test method for {@link com.reddit.application.service.CommentsService#save(com.reddit.application.dto.CommentDTO)}.
	 */
//	@Test
//	void testSave() {
//		fail("Not yet implemented");
//	}

	/**
	 * Test method for {@link com.reddit.application.service.CommentsService#getAllCommentsForPost(java.lang.Long)}.
	 */
//	@Test
//	void testGetAllCommentsForPostLong() {
//		fail("Not yet implemented");
//	}

	/**
	 * Test method for {@link com.reddit.application.service.CommentsService#getAllCommentsForPost(java.lang.String)}.
	 */
//	@Test
//	void testGetAllCommentsForPostString() {
//		fail("Not yet implemented");
//	}

	/**
	 * Test method for {@link com.reddit.application.service.CommentsService#containSwearWords(java.lang.String)}.
	 */
	@Test
	@DisplayName("Test Should Pass When Comment do not Contains Swear Words")
	public void testContainSwearWords() {
		CommentsService commentsService = new CommentsService(null, null, null, null, null, null, null, null);
		assertThat(commentsService.containSwearWords("This is a comment")).isFalse();
	}
	
	@Test
	@DisplayName("Test Should Fail When Comment Contains Swear Words")
	public void shouldFailWithCommentContainsSwearWords() {
		CommentsService commentsService = new CommentsService(null, null, null, null, null, null, null, null);
		assertThatThrownBy(() -> {
			commentsService.containSwearWords("This is a shit comment");
		}).isInstanceOf(SpringRedditException.class).hasMessage("Comments contain unacceptable language");
	}

}
