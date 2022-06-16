package com.api.authorization.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.api.authorization.model.ForgetRequest;
import com.api.authorization.model.LoginRequest;
import com.api.authorization.model.NewPasswordRequest;
import com.api.authorization.model.RegisterRequest;
import com.api.authorization.model.ValidationRespose;
import com.api.authorization.service.UsersDataService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
public class AuthorizationControllerTests {
	private MockMvc mockMvc;
	@Autowired
	private WebApplicationContext context;
	private LoginRequest loginRequest;
	@InjectMocks
	AuthorizationController controller;
	@Mock
	UsersDataService usersDataService;

	@BeforeEach
	public void setUp() throws Exception {
		loginRequest = new LoginRequest();
		loginRequest.setUserId("annu.sharma9830@gmail.com");
		loginRequest.setPassword("annu@116");
		
		mockMvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
	}

	@Test
	public void testAuthenticationisSuccessfulwhenTheUserExists() throws Exception {
		mockMvc.perform(post("/auth/login").contentType(MediaType.APPLICATION_JSON)
				.content(new ObjectMapper().writeValueAsString(loginRequest))).andExpect(status().isOk());
	}

	@Test
	public void testAuthenticationFailedwhenInvalidCredential() throws Exception {
		LoginRequest credentials = new LoginRequest();
		credentials.setUserId("testid");
		credentials.setPassword("testPass");
		mockMvc.perform(post("/auth/login").contentType(MediaType.APPLICATION_JSON)
				.content(new ObjectMapper().writeValueAsString(credentials))).andExpect(status().isBadRequest());
	}

	@Test
	public void testAuthenticationisValidToken() throws Exception {

		String token = getAuthToken();
		mockMvc.perform(get("/auth/validate").header("Authorization", "Bearer " + token)).andExpect(status().isOk());
	}
	
	@Test
	public void testregisterUserSuccess() throws Exception {
		
		when(usersDataService.registerUser(any())).thenReturn(new RegisterRequest());
		RegisterRequest registerRequest=new RegisterRequest();
		registerRequest.setUserId("testUserId");
		registerRequest.setFirstName("test");
		registerRequest.setLastName("test");
		registerRequest.setDateOfBirth(LocalDate.now());
		registerRequest.setGender("Male");
		registerRequest.setMobileNo("1234567890");
		registerRequest.setPassword("test");
		ResponseEntity<ValidationRespose> response = controller.register(registerRequest);
		assertEquals(200, response.getStatusCodeValue());
	}
	@Test
	public void testregisterUserFail() throws Exception {
		RegisterRequest registerRequest=new RegisterRequest();
		registerRequest.setUserId("annu.sharma9830@gmail.com");
		registerRequest.setFirstName("test");
		registerRequest.setLastName("test");
		registerRequest.setDateOfBirth(LocalDate.now());
		registerRequest.setGender("Male");
		registerRequest.setMobileNo("1234567890");
		registerRequest.setPassword("test");
		mockMvc.perform(post("/auth/register").contentType(MediaType.APPLICATION_JSON)
		.content(new ObjectMapper().findAndRegisterModules().writeValueAsString(registerRequest))).andExpect(status().isBadRequest());
	}
	
	@Test
	public void testLogout() throws Exception {
		String token = getAuthToken();
		mockMvc.perform(get("/auth/logout").header("Authorization", "Bearer " + token)).andExpect(status().isOk());
	}
	
	@Test
	public void testGetAllUsersSuccess() throws Exception {

		String token = getAuthToken();
		mockMvc.perform(get("/auth/getAllUsers").header("Authorization", "Bearer " + token))
		.andExpect(status().isOk());
	}
	@Test
	public void testGetAllUsersFail() throws Exception {
		
		mockMvc.perform(get("/auth/getAllUsers").header("Authorization", "Bearer " + null))
		.andExpect(status().isForbidden());
	}
	@Test
	public void testgetUserByUserIdSuccess() throws Exception {

		String token = getAuthToken();
		mockMvc.perform(get("/auth/getUserByUserId").header("Authorization", "Bearer " + token))
		.andExpect(status().isOk());
	}
	@Test
	public void testgetUserByUserIdFail() throws Exception {
		
		mockMvc.perform(get("/auth/getUserByUserId").header("Authorization", "Bearer " + null))
		.andExpect(status().isForbidden());
	}
	@Test
	public void testForgetPasswordSuccess() throws Exception {
		when(usersDataService.forgetPassword(any())).thenReturn(true);
		ForgetRequest forgetRequest=new ForgetRequest();
		forgetRequest.setUserId("test");
		forgetRequest.setDateOfBirth(LocalDate.now());
		forgetRequest.setMobileNo("9874563210");
		ResponseEntity<ValidationRespose> response = controller.forget(forgetRequest);
		assertEquals(200, response.getStatusCodeValue());
		
	}
	@Test
	public void testForgetPasswordFail() throws Exception {
		ForgetRequest forgetRequest=new ForgetRequest();
		forgetRequest.setUserId("test");
		forgetRequest.setDateOfBirth(LocalDate.now());
		forgetRequest.setMobileNo("9874563210");
		mockMvc.perform(post("/auth/forget").contentType(MediaType.APPLICATION_JSON)
				.content(new ObjectMapper().findAndRegisterModules()
						.writeValueAsString(forgetRequest))).andExpect(status().isBadRequest());
	}
	@Test
	public void testUpdatePasswordSuccess() throws Exception {
		
		when(usersDataService.updatePassword(any())).thenReturn(true);
		
		NewPasswordRequest newPasswordRequest=new NewPasswordRequest();
		newPasswordRequest.setUserId("test");
		newPasswordRequest.setNewPassword("test");
		ResponseEntity<ValidationRespose> response = controller.updatePassword(newPasswordRequest);
		assertEquals(200, response.getStatusCodeValue());
	}
	@Test
	public void testUpdatePasswordFail() throws Exception  {
		NewPasswordRequest newPasswordRequest=new NewPasswordRequest();
		newPasswordRequest.setUserId("test");
		newPasswordRequest.setNewPassword("test");
		mockMvc.perform(post("/auth/updatePassword").contentType(MediaType.APPLICATION_JSON)
				.content(new ObjectMapper().findAndRegisterModules()
						.writeValueAsString(newPasswordRequest))).andExpect(status().isBadRequest());
	}
	
	private String getAuthToken() throws Exception {
		MvcResult mvcResult = mockMvc.perform(post("/auth/login").contentType(MediaType.APPLICATION_JSON)
				.content(new ObjectMapper().writeValueAsString(loginRequest))).andReturn();
		String response = mvcResult.getResponse().getContentAsString();
		ObjectMapper mapper = new ObjectMapper();
		Map<String, String> jsonMap = new HashMap<String, String>();
		// convert JSON string to Map
		jsonMap = mapper.readValue(response, new TypeReference<Map<String, String>>() {
		});
		return jsonMap.get("authToken");
	}
	

}
