package com.daw.user.controller;

import java.util.Date;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.daw.user.repository.UserRepository;
import com.daw.user.config.JwtTokenUtil;
import com.daw.user.dto.UserDto;
import com.daw.user.exception.ResourceNotFoundException;
import com.daw.user.model.User;

@CrossOrigin
@RestController
@RequestMapping("/api/v1")
public class UserController {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder bcryptEncoder;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	private final String TEMPLATE_NOT_FOUND = "User not found for id = ";
	private final String PATH = "/user";

	@GetMapping(PATH)
	public Page<User> getAllUser(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size) {
		Page<User> findAll = userRepository.findAll(PageRequest.of(page, size));
		return (Page<User>) findAll;
	}

	@GetMapping(PATH + "/{id}")
	public ResponseEntity<User> get(@PathVariable Long id) throws ResourceNotFoundException {
		User user = userRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(TEMPLATE_NOT_FOUND + id));
		return ResponseEntity.ok().body(user);
	}

	@PostMapping(PATH)
	public ResponseEntity<User> createUser(@Valid @RequestBody UserDto userDto) {
		User user = new User();
		user.setEmail(userDto.getEmail());
		user.setUsername(userDto.getUsername());
		user.setPassword(bcryptEncoder.encode(userDto.getPassword()));

		user.setCreatedOn(new Date());
		user.setCreatedBy(
				jwtTokenUtil.getUserId((String) SecurityContextHolder.getContext().getAuthentication().getDetails()));
		return ResponseEntity.ok(userRepository.save(user));
	}

	@PutMapping(PATH + "/{id}")
	public ResponseEntity<User> updateUser(@PathVariable Long id, @Valid @RequestBody UserDto userDto)
			throws ResourceNotFoundException {
		User user = userRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(TEMPLATE_NOT_FOUND + id));
		user.setEmail(userDto.getEmail());
		user.setUsername(userDto.getUsername());
		user.setPassword(bcryptEncoder.encode(userDto.getPassword()));

		user.setUpdatedOn(new Date());
		user.setUpdatedBy(
				jwtTokenUtil.getUserId((String) SecurityContextHolder.getContext().getAuthentication().getDetails()));
		return ResponseEntity.ok(userRepository.save(user));
	}

	@DeleteMapping(PATH + "/{id}")
	public ResponseEntity<?> deleteUser(@PathVariable Long id) throws ResourceNotFoundException {
		User user = userRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(TEMPLATE_NOT_FOUND + id));
		userRepository.delete(user);
		return ResponseEntity.noContent().build();
	}

}
