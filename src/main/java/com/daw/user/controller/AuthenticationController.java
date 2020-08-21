package com.daw.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.daw.user.config.JwtTokenUtil;
import com.daw.user.dto.LoginDto;
import com.daw.user.dto.LoginResponse;
import com.daw.user.model.DataToken;
import com.daw.user.model.User;
import com.daw.user.repository.UserRepository;

@CrossOrigin
@RestController
@RequestMapping("/api/v1")
public class AuthenticationController {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Autowired
	private UserRepository userRepository;

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody LoginDto loginDto) throws Exception {
		authenticate(loginDto.getUsername(), loginDto.getPassword());
		final User user = userRepository.findByUsername(loginDto.getUsername());
		return ResponseEntity.ok(new LoginResponse(jwtTokenUtil.generateToken(user)));
	}

	@GetMapping("/get-user/{token}")
	public DataToken getUserFromToken(@PathVariable String token) {
		DataToken data = new DataToken();
		if (!jwtTokenUtil.isTokenExpired(token)) {
			User user = userRepository.findById(jwtTokenUtil.getUserId(token)).orElse(new User());
			data.setId(user.getId());
			data.setEmail(user.getEmail());
			data.setUsername(user.getUsername());
			data.setPassword(user.getPassword());
			data.setTokenExpired(false);
		} else {
			data.setTokenExpired(true);
		}
		return data;
	}

	private void authenticate(String username, String password) throws Exception {
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
		} catch (DisabledException e) {
			throw new Exception("USER_DISABLED", e);
		} catch (BadCredentialsException e) {
			throw new Exception("Invalid Username or Password", e);
		}
	}
}