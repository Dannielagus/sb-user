package com.daw.user.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class LoginDto {

	@NotNull(message = "Username cannot be null")
	@Size(min = 3, max = 50, message = "Minimum 3 characters")
	private String username;

	@NotNull(message = "Password cannot be null")
	@Size(min = 3, max = 100, message = "Minimum 3 characters")
	private String password;

}