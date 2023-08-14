package com.poluhin.ss.demo.controller;

import static com.poluhin.ss.demo.sample.RegistrationUserSample.registrationUser_sample1;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import com.poluhin.ss.demo.domain.model.RegistrationUser;
import com.poluhin.ss.demo.domain.model.User;
import com.poluhin.ss.demo.domain.model.jwt.JwtRequest;
import com.poluhin.ss.demo.domain.model.jwt.JwtResponse;
import com.poluhin.ss.demo.exceptions.AuthError;
import com.poluhin.ss.demo.service.AuthService;

import lombok.SneakyThrows;
import lombok.val;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.Resource;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {
	private final static String USER_NAME = "test_user";
	private final static String PASSWORD = "test_user1";
	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private AuthService authService;

	private static final String token = "jwt_token";

	@Value("classpath:jwt_request_sample.json")
	private Resource jwtRequestSampleJson;

	@Value("classpath:registration_user_sample.json")
	private Resource RegistrationUserSampleJson;

	@Test
	@SneakyThrows
	void createAuthToken_Success() {
		String expectedResult = "{\"token\":\"jwt_token\"}";
		ResponseEntity<?> responseJwt = ResponseEntity.ok(new JwtResponse(token));

		when(authService.createAuthToken(new JwtRequest(USER_NAME, PASSWORD)))
				.thenReturn((ResponseEntity) responseJwt);

		MvcResult mvcResult = mockMvc.perform(
						MockMvcRequestBuilders.post("/auth")
								.contentType(MediaType.APPLICATION_JSON)
								.content(getResourceAsString(jwtRequestSampleJson)))
				.andExpect(status().isOk())
				.andReturn();

		val result = mvcResult.getResponse().getContentAsString();

		assertNotNull(result);
		assertEquals(expectedResult, result);
	}

	@Test
	@SneakyThrows
	void createAuthTokenWrongLoginOrPassword_UnSuccess() {
		ResponseEntity<?> responseUnauthorized = new ResponseEntity<>(new AuthError(HttpStatus.UNAUTHORIZED.value(),
		                                                                        "Wrong login or password"),
		                                                          HttpStatus.UNAUTHORIZED);

		when(authService.createAuthToken(new JwtRequest(USER_NAME, PASSWORD)))
				.thenReturn((ResponseEntity) responseUnauthorized);

		MvcResult mvcResult = mockMvc.perform(
						MockMvcRequestBuilders.post("/auth")
								.contentType(MediaType.APPLICATION_JSON)
								.content(getResourceAsString(jwtRequestSampleJson)))
				.andExpect(status().isUnauthorized())
				.andReturn();

		val result = mvcResult.getResponse().getContentAsString();
		assertNotNull(result);
	}

	@Test
	@SneakyThrows
	void createNewUser_Success() {
		String expectedResult = "{\"id\":1,\"username\":\"test_user\"}";
		ResponseEntity<?> responseNewUser = ResponseEntity.ok(new User(1L, USER_NAME));

		when(authService.createNewUser(registrationUser_sample1))
				.thenReturn((ResponseEntity) responseNewUser);

		MvcResult mvcResult = mockMvc.perform(
						MockMvcRequestBuilders.post("/registration")
								.contentType(MediaType.APPLICATION_JSON)
								.content(getResourceAsString(RegistrationUserSampleJson)))
				.andExpect(status().isOk())
				.andReturn();

		val result = mvcResult.getResponse().getContentAsString();

		assertNotNull(result);
		assertEquals(expectedResult, result);
	}

	@Test
	@SneakyThrows
	void createNewUserUserAlreadyExits_UnSuccess() {
		ResponseEntity<?> responseBadRequest = new ResponseEntity<>(new AuthError(HttpStatus.BAD_REQUEST.value(),
		                                                                        "UserAlreadyExits"),
		                                                          HttpStatus.BAD_REQUEST);

		when(authService.createNewUser(registrationUser_sample1))
				.thenReturn((ResponseEntity) responseBadRequest);

		MvcResult mvcResult = mockMvc.perform(
						MockMvcRequestBuilders.post("/registration")
								.contentType(MediaType.APPLICATION_JSON)
								.content(getResourceAsString(RegistrationUserSampleJson)))
				.andExpect(status().isBadRequest())
				.andReturn();

		val result = mvcResult.getResponse().getContentAsString();
		assertNotNull(result);
	}



	@SneakyThrows
	private String getResourceAsString(Resource resource) {
		return IOUtils.toString(resource.getInputStream());
	}
}