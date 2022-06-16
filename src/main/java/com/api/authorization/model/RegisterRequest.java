package com.api.authorization.model;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RegisterRequest {

	private String userId;
	private String password;
	private String firstName;
	private String lastName;
	private String gender;
	private LocalDate dateOfBirth;
	private String mobileNo;
	
	
}
