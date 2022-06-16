package com.api.authorization.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.api.authorization.dao.UsersDAO;
import com.api.authorization.entity.Users;
import com.api.authorization.exception.InvalidPasswordException;
import com.api.authorization.exception.UserNotFoundException;
import com.api.authorization.exception.UsernameAlreadyExistException;
import com.api.authorization.model.ForgetRequest;
import com.api.authorization.model.NewPasswordRequest;
import com.api.authorization.model.RegisterRequest;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@Setter
public class UsersDataService implements UserDetailsService {
	@Autowired
	private UsersDAO usersDao;
	
	@Autowired
	PasswordEncoder passwordEncoder;

	@Override
	public UserDetails loadUserByUsername(String uid) {
		log.info("Entering loadUserByUserName service method!!!");
		Users user = usersDao.findById(uid)
				.orElseThrow(() -> new UsernameNotFoundException("User does not exist for the username:"));
		log.info("User : {}",user.toString());
		log.info("Exiting loadUserByUserName service method!!!");
		return new User(user.getUserId(), user.getPassword(), new ArrayList<>());

	}
	
	public boolean logout(String uid) {
		log.info("Entering logout service method!!!");
		Users user = usersDao.findById(uid)
				.orElseThrow(UserNotFoundException::new);
		log.info("User : {}",user.toString());
		user.setActive(false);
		user.setLastSeen(LocalDateTime.now());
		usersDao.save(user);
		log.info("Exiting logout service method!!!");
		return true;

	}
	
	public boolean forgetPassword(ForgetRequest forgetRequest) {
		log.info("Entering forgetPassword service method!!!");
		Users user = usersDao.findById(forgetRequest.getUserId())
				.orElseThrow(UserNotFoundException::new);
		log.info("User : {}",user.toString());
		log.info("Exiting forgetPassword service method!!!");
		return forgetRequest.getDateOfBirth().isEqual(user.getDateOfBirth()) && forgetRequest.getMobileNo().equals(user.getMobileNo());
			
		
	}
	public boolean updatePassword(NewPasswordRequest newPassword) {
		log.info("Entering updatePassword service method!!!");
		Users user = usersDao.findById(newPassword.getUserId())
				.orElseThrow(UserNotFoundException::new);
		log.info("User : {}",user.toString());
		log.info("Exiting updatePassword service method!!!");
		user.setPassword(passwordEncoder.encode(newPassword.getNewPassword()));
		usersDao.save(user);
		return true;
	}
	

	public UserDetails loadUserByUsername(String uid, String password) {
		log.info("Entering loadUserByUserName and password service method!!!");
		Users user = usersDao.findById(uid)
				.orElseThrow(UserNotFoundException::new);
		log.info("User : {}",user.toString());
		if (passwordEncoder.matches(password ,user.getPassword())) {
			log.info("Exiting loadUserByUserName service method!!!");
			user.setActive(true);
			usersDao.save(user);
			return new User(user.getUserId(), user.getPassword(), new ArrayList<>());
		} else
			throw new InvalidPasswordException();

	}
	
	public RegisterRequest registerUser(RegisterRequest registerRequest) {
		log.info("Entering registerUser service method!!!");
		Users users=usersDao.findById(registerRequest.getUserId()).orElse(null);
		if(null == users) {
			log.info("User : {}",registerRequest.toString());
			Users newUser = new Users();
			newUser.setDateOfBirth(registerRequest.getDateOfBirth());
			newUser.setFirstName(registerRequest.getFirstName());
			newUser.setLastName(registerRequest.getLastName());
			newUser.setGender(registerRequest.getGender());
			newUser.setMobileNo(registerRequest.getMobileNo());
			newUser.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
			newUser.setUserId(registerRequest.getUserId());
			newUser.setActive(false);
			newUser.setLastSeen(LocalDateTime.now());
			usersDao.save(newUser);
			return registerRequest;		
		}
		else throw new UsernameAlreadyExistException();
		
	}

	public List<Users> getAllUser() {
		log.info("Entering getAllUser method");
		log.info("Exiting getAllUser method");
		return usersDao.findAll();
		
	}

	public Users getuserByUserId(String userId) {
		log.info("Entering getAllUser method");
		log.info("Exiting getAllUser method");
		return usersDao.findById(userId).orElse(null);
	}

}
