package com.daw.user.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class DataToken {

	private long id;
	private String email;
	private String username;
	private String password;
	private boolean isTokenExpired;

}
