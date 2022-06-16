package com.api.authorization.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.authorization.model.RegisterRequest;
import com.api.authorization.entity.Users;
import com.api.authorization.model.ForgetRequest;
import com.api.authorization.model.LoginRequest;
import com.api.authorization.model.LoginResponse;
import com.api.authorization.model.NewPasswordRequest;
import com.api.authorization.model.ValidationRespose;
import com.api.authorization.service.UsersDataService;


import com.api.authorization.service.JwtUtil;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/auth")
@Slf4j
public class AuthorizationController {
	
	@Autowired
	private JwtUtil jwtutil;
	@Autowired
	private UsersDataService userDataService;

	@PostMapping(value = "/login")
	public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest userLoginCredentials) {
		log.info("Entering login controller method!!!");
		log.info("User ID: {}", userLoginCredentials.getUserId());
		log.info("User password: {}", userLoginCredentials.getPassword());
		final UserDetails userdetails = userDataService.loadUserByUsername(userLoginCredentials.getUserId(),
				userLoginCredentials.getPassword());
		log.info("Exiting login controller method!!!");
		return new ResponseEntity<>(new LoginResponse(jwtutil.generateToken(userdetails),userLoginCredentials.getUserId()), HttpStatus.OK);
	}
	
	@PostMapping(value="/register")
	public ResponseEntity<ValidationRespose> register(@RequestBody RegisterRequest userRegister) {
		log.info("Entering register controller method!!!");
		log.info("User Details: {}", userRegister.toString());
		userDataService.registerUser(userRegister);
		log.info("Exiting register controller method!!!");
		return new ResponseEntity<>(new ValidationRespose(true), HttpStatus.OK);
	}
	
	
	
	@PostMapping(value="/forget")
	public ResponseEntity<ValidationRespose> forget(@RequestBody ForgetRequest forgetRequest) {
		log.info("Entering forget controller method!!!");
		log.info("User Details: {}", forgetRequest.toString());
		if(userDataService.forgetPassword(forgetRequest)) {
			log.info("Exiting forget controller method!!!");
			return new ResponseEntity<>(new ValidationRespose(true), HttpStatus.OK);
		}
		else {
			log.info("Exiting forget controller method!!!");
			return new ResponseEntity<>(new ValidationRespose(false), HttpStatus.OK);
		}
		
	}
	
	@PostMapping(value="/updatePassword")
	public ResponseEntity<ValidationRespose> updatePassword(@RequestBody NewPasswordRequest newPassword) {
		log.info("Entering updatePassword controller method!!!");
		log.info("User New Password: {}", newPassword.toString());
		if(userDataService.updatePassword(newPassword)) {
			log.info("Exiting updatePassword controller method!!!");
			return new ResponseEntity<>(new ValidationRespose(true), HttpStatus.OK);
		}
		else {
			log.info("Exiting updatePassword controller method!!!");
			return new ResponseEntity<>(new ValidationRespose(false), HttpStatus.OK);
		}
		
	}
	@GetMapping(value="/logout")
	public ResponseEntity<ValidationRespose> logout(@RequestHeader("Authorization") String token) {
		log.info("Entering logout controller method!!!");
		log.info("Exiting logout controller method!!!");
		String localToken = token.substring(7);
		String userName = jwtutil.extractUsername(localToken);
		if(userDataService.logout(userName))
			return new ResponseEntity<>(new ValidationRespose(true), HttpStatus.OK);
		else 
			return new ResponseEntity<>(new ValidationRespose(false), HttpStatus.OK);
		
	}
	

	@GetMapping(value = "/validate")
	public ResponseEntity<ValidationRespose> getValidity(@RequestHeader("Authorization") String token) {
		log.info("Entering getValidity controller method!!!");
		log.info("Token: {}",token);
		String localToken = token.substring(7);
		
		ValidationRespose response = new ValidationRespose();
		if (null!= localToken && jwtutil.validateToken(localToken)) {
			response.setValid(true);
		}
		log.info(response.toString());
		log.info("Exiting getValidity controller method!!!");
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@GetMapping(value="/getUserByUserId")
	public ResponseEntity<Users> getUserByUserId(@RequestHeader("Authorization") String token) {
		log.info("Entering getUserByUserId controller method!!!");
		log.info("Token: {}",token);
		String localToken = token.substring(7);
		ValidationRespose response = new ValidationRespose();
		if (null!= localToken && jwtutil.validateToken(localToken)) {
			response.setValid(true);
		}
		log.info(response.toString());
		if(!response.isValid())return new ResponseEntity<>(new Users(),HttpStatus.FORBIDDEN);
		Users user = userDataService.getuserByUserId(jwtutil.extractUsername(localToken));
		log.info("Exiting getUserByUserId controller method!!!");
		return new ResponseEntity<>(user, HttpStatus.OK);
		 
	}
	@GetMapping(value="/getAllUsers")
	public ResponseEntity<List<Users>> getAllUsers(@RequestHeader("Authorization") String token) {
		log.info("Entering getAllUsers controller method!!!");
		log.info("Token: {}",token);
		String localToken = token.substring(7);
		ValidationRespose response = new ValidationRespose();
		if (null!= localToken && jwtutil.validateToken(localToken)) {
			response.setValid(true);
		}
		log.info(response.toString());
		if(!response.isValid())return new ResponseEntity<>(new ArrayList<>(),HttpStatus.FORBIDDEN);
		log.info("Exiting getAllUsers controller method!!!");
		return new ResponseEntity<>(userDataService.getAllUser(), HttpStatus.OK);
		 
	}

}
