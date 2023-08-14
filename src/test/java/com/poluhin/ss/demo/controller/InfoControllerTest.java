package com.poluhin.ss.demo.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import com.poluhin.ss.demo.config.TestSecurityConfig;
import com.poluhin.ss.demo.utils.JwtTokenUtils;
import java.util.List;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@SpringBootTest
@AutoConfigureMockMvc
class InfoControllerTest {
	private final static String TEST_USER = "test_user";
	private final static String PASSWORD = "test_user1";

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	JwtTokenUtils jwtTokenUtils;
	@SneakyThrows
	@Test
	void unsecuredData_Success() {
		String result = getSuccessAnswerFromUrl("/info/unsecured");

		assertNotNull(result);
		assertEquals("unsecured data", result);
	}

	@SneakyThrows
	@Test
	@WithMockUser(username = TEST_USER,password = PASSWORD,roles = {"USER"})
	void securedDataLoginByUser_Success() {
		String result = getSuccessAnswerFromUrl("/info/secured");

		assertNotNull(result);
		assertEquals("secured data", result);
	}
	@Test
	@SneakyThrows
	void securedDataAuthByJwtToken_Success() {
		final String ROLE_USER = "ROLE_USER";
		UserDetails userDetailsSample1 = new User(
				TEST_USER,
				PASSWORD,
				List.of(new SimpleGrantedAuthority(ROLE_USER)));
		String token = TestSecurityConfig.createToken(userDetailsSample1);
		when(jwtTokenUtils.getUsername(any(String.class))).thenReturn(TEST_USER);
		when(jwtTokenUtils.getRoles(any(String.class))).thenReturn(List.of(ROLE_USER));

		MvcResult mvcResult = mockMvc.perform(
						get("/info/secured")
								.header("Authorization", "Bearer " + token))
				.andExpect(status().isOk())
				.andReturn();
		String result = mvcResult.getResponse().getContentAsString();

		assertNotNull(result);
		assertEquals("secured data", result);
	}

	@SneakyThrows
	@Test
	void securedData_UnSuccess() {
		String result = get4xxStatusFromUrl("/info/secured");

		assertNotNull(result);
	}

	@SneakyThrows
	@Test
	@WithMockUser(username = TEST_USER,password = PASSWORD,roles = {"ADMIN"})
	void adminDataLoginByAdmin_Success() {
		String result = getSuccessAnswerFromUrl("/info/admin");

		assertNotNull(result);
		assertEquals("admin data", result);
	}

	@Test
	@SneakyThrows
	void adminDataAuthByJwtToken_Success() {
		final String ROLE_ADMIN = "ROLE_ADMIN";
		UserDetails userDetailsSample1 = new User(TEST_USER,
		                                          PASSWORD,
		                                          List.of(new SimpleGrantedAuthority(ROLE_ADMIN)));
		String token = TestSecurityConfig.createToken(userDetailsSample1);
		when(jwtTokenUtils.getUsername(any(String.class))).thenReturn(TEST_USER);
		when(jwtTokenUtils.getRoles(any(String.class))).thenReturn(List.of(ROLE_ADMIN));

		MvcResult mvcResult = mockMvc.perform(
						get("/info/admin")
								.header("Authorization", "Bearer " + token))
				.andExpect(status().isOk())
				.andReturn();
		String result = mvcResult.getResponse().getContentAsString();

		assertNotNull(result);
		assertEquals("admin data", result);
	}

	@SneakyThrows
	@Test
	@WithMockUser(username = TEST_USER,password = PASSWORD,roles = {"USER"})
	void adminDataLoginByUser_UnSuccess() {

		String result = get4xxStatusFromUrl("/info/admin");

		assertNotNull(result);
	}

	@SneakyThrows
	@Test
	void adminData_UnSuccess() {

		String result = get4xxStatusFromUrl("/info/admin");

		assertNotNull(result);
	}

	@SneakyThrows
	private String getSuccessAnswerFromUrl(String url) {
		MvcResult mvcResult = mockMvc.perform(
						get(url)
								.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn();

		return mvcResult.getResponse().getContentAsString();
	}

	@SneakyThrows
	private String get4xxStatusFromUrl(String url) {
		MvcResult mvcResult = mockMvc.perform(
						get(url)
								.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().is4xxClientError())
				.andReturn();

		return mvcResult.getResponse().getContentAsString();
	}
}