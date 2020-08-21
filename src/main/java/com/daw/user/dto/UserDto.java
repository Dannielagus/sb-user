package com.daw.user.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserDto extends LoginDto {

	@Email(message = "Email should be valid")
	@Size(max = 50, message = "Maximum 50 characters")
	private String email;

}
