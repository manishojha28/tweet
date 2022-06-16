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
public class ForgetRequest {
	
	private String userId;
	private String mobileNo;
	private LocalDate dateOfBirth;

}
