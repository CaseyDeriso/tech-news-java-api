package com.technews.controller;

import com.technews.model.Post;
import com.technews.repository.PostRepository;
import com.technews.repository.UserRepository;
import com.technews.repository.VoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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

	@PutMapping("api/posts/{id}")
	public Post updatePost(@PathVariable int id, @RequestBody Post post) {
		Post tempPost = repository.getOne(id);
		tempPost.setTitle(post.getTitle());

		return repository.save(tempPost);
	}
}
