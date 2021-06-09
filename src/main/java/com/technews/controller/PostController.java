package com.technews.controller;

import com.technews.model.Post;
import com.technews.model.User;
import com.technews.model.Vote;
import com.technews.repository.PostRepository;
import com.technews.repository.UserRepository;
import com.technews.repository.VoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
public class PostController {
	@Autowired
	PostRepository repository;

	@Autowired
	VoteRepository voteRepository;

	@Autowired
	UserRepository userRepository;

	@GetMapping("/api/posts")
	public Post getPost(@PathVariable Integer id) {
		Post returnPost = repository.getOne(id);
		returnPost.setVoteCount(voteRepository.countVotesByPostId(returnPost.getId()));

		return returnPost;
	}

	@PostMapping("/api/posts")
	@ResponseStatus(HttpStatus.CREATED)
	public Post addPost(@RequestBody Post post) {
		repository.save(post);
		return post;
	}

	@PutMapping("/api/posts/{id}")
	public Post updatePost(@PathVariable int id, @RequestBody Post post) {
		Post tempPost = repository.getOne(id);
		tempPost.setTitle(post.getTitle());

		return repository.save(tempPost);
	}

	@PutMapping("/api/posts/upvote")
	public String addVote(@RequestBody Vote vote, HttpServletRequest request) {
		String returnValue = "";

		if (request.getSession(false) != null) {
			Post returnPost = null;

//			if logged in, grab session object and set the vote's userId to the sessions.getId() return value
			User sessionUser = (User) request.getSession().getAttribute("SESSION_USER");
			vote.setUserId(sessionUser.getId());
			voteRepository.save(vote);

//			count the total votes and set the new value
			returnPost = repository.getOne(vote.getPostId());
			returnPost.setVoteCount(voteRepository.countVotesByPostId(vote.getPostId()));

//			set return value to empty string
			returnValue = "";
		} else {
//			return "login" and prompt user to login in order to vote.
			returnValue = "login";
		}
		return returnValue;
	}

	@DeleteMapping("/api/posts/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deletePost(@PathVariable int id) {
		repository.deleteById(id);
	}
}
