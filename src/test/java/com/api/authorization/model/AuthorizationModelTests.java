package com.api.authorization.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
public class AuthorizationModelTests {

	@Test
	public void gettersAndSettersValidationResponseTest() {
		ValidationRespose validationRespose = new ValidationRespose();
		validationRespose.setValid(true);
		assertEquals(true, validationRespose.isValid());

	}

	@Test
	public void constructorValidationResponseTest() {
		ValidationRespose responseNoArg = new ValidationRespose();
		assertNotNull(responseNoArg);
		ValidationRespose responseAllArg = new ValidationRespose(true);
		assertNotNull(responseAllArg);
	}

	@Test
	public void gettersAndSettersUserTokenTest() {
		LoginResponse userToken = new LoginResponse();
		userToken.setAuthToken("token");
		assertEquals("token", userToken.getAuthToken());
	}

	@Test
	public void constructorUserTokenTest() {
		LoginResponse userTokenNoArg = new LoginResponse();
		LoginResponse userTokenAllArg = new LoginResponse();
		assertNotNull(userTokenNoArg);
		assertNotNull(userTokenAllArg);
	}

	@Test
	public void constructorAppUserTest() {
		LoginRequest appUserNoArg = new LoginRequest();
		assertNotNull(appUserNoArg);
		LoginRequest appUserAllArg = new LoginRequest("userid", "upass");
		assertNotNull(appUserAllArg);

	}

}
