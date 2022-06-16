package com.api.authorization.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.DynamicUpdate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@DynamicUpdate
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Users {
	
	@Id
	@NotBlank(message = "ID cannot be null")
	private String userId;
	@NotBlank(message = "Please enter password")
	@Min(value = 4, message = "Password must contain 4 character")
	private String password;
	@NotNull
	@NotBlank(message = "Please Enter name")
	private String firstName;
	private String lastName;
	private String gender;
	private LocalDate dateOfBirth;
	private LocalDateTime lastSeen;
	private boolean isActive;
	@NotNull
	@NotBlank(message = "Please Enter Mobile No")
	private String mobileNo;	

}
