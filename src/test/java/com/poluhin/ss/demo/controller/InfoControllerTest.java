package com.poluhin.ss.demo.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@SpringBootTest
@AutoConfigureMockMvc
class InfoControllerTest {

	private final static String LOGIN_URL = "http://localhost/login";
	@Autowired
	private MockMvc mockMvc;

	@SneakyThrows
	@Test
	void unsecuredData_Success() {
		String result = getAnswerFromUrl("/info/unsecured");

		assertNotNull(result);
		assertEquals("unsecured data", result);
	}

	@SneakyThrows
	@Test
	@WithMockUser(username = "test_user",password = "test_user1",roles = {"USER"})
	void securedDataLoginByUser_Success() {
		String result = getAnswerFromUrl("/info/secured");

		assertNotNull(result);
		assertEquals("secured data", result);
	}

	@SneakyThrows
	@Test
	void securedDataNotLoginUser_RedirectToLogin() {
		String result  =redirectToLoginFromUrl("/info/secured");


		assertNotNull(result);
	}

	@SneakyThrows
	@Test
	@WithMockUser(username = "test_admin",password = "test_admin1",roles = {"ADMIN"})
	void adminDataLoginByAdmin_Success() {
		String result = getAnswerFromUrl("/info/admin");

		assertNotNull(result);
		assertEquals("admin data", result);
	}

	@SneakyThrows
	@Test
	void adminDataNotLoginUser_RedirectToLogin() {
		String result = redirectToLoginFromUrl("/info/admin");

		assertNotNull(result);
	}

	@SneakyThrows
	@Test
	@WithMockUser(username = "test_user",password = "test_user1",roles = {"USER"})
	void adminDataLoginByUser_UnSuccess() {
		MvcResult mvcResult = mockMvc.perform(
						get("/info/admin")
								.content(String.valueOf(MediaType.APPLICATION_JSON)))
				.andExpect(status().is4xxClientError())
				.andReturn();

		String result = mvcResult.getResponse().getContentAsString();

		assertNotNull(result);
	}

	@SneakyThrows
	private String getAnswerFromUrl(String url) {
		MvcResult mvcResult = mockMvc.perform(
						get(url)
								.content(String.valueOf(MediaType.APPLICATION_JSON)))
				.andExpect(status().isOk())
				.andReturn();

		return mvcResult.getResponse().getContentAsString();
	}

	@SneakyThrows
	private String redirectToLoginFromUrl(String urlToRedirect) {
		MvcResult mvcResult = mockMvc.perform(
						get(urlToRedirect)
								.content(String.valueOf(MediaType.APPLICATION_JSON)))
				.andExpect(redirectedUrl(LOGIN_URL))
				.andReturn();

		return mvcResult.getResponse().getContentAsString();
	}
}