package com.technews.controller;

import com.technews.model.Post;
import com.technews.model.User;
import com.technews.repository.UserRepository;
import com.technews.repository.VoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserController {
	@Autowired
	UserRepository repository;

	@Autowired
	VoteRepository voteRepository;

	@GetMapping("/api/users")
	public List<User> getAllUsers() {
		List<User> userList = repository.findAll();
		for (User u: userList) {
			List<Post> postList = u.getPosts();
			for (Post p : postList) {
				p.setVoteCount(voteRepository.countVotesByPostId(p.getId()));
			}
		}
		return userList;
	}

	@GetMapping("/api/users/{id}")
	public User getUserById(@PathVariable Integer id) {
		User returnUser = repository.getOne(id);
		List<Post> postList = returnUser.getPosts();
		for (Post p : postList) {
			p.setVoteCount(voteRepository.countVotesByPostId(p.getId()));
		}
		return returnUser;
	}

}