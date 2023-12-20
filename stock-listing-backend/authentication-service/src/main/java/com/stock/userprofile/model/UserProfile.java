package com.stock.userprofile.model;

import java.util.Date;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserProfile {
	private long id;

	private String username;
	private String firstName;
	private String lastName;
	private long number;
	private Date dateOfBirth;
	private String email;

	private Set<String> roles;

	private String password;

	private String securityQuestion;

	private String securityAnswer;

}