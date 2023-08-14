package com.poluhin.ss.demo.controller;

import static com.poluhin.ss.demo.sample.ResourceObjectSample.resourceObject_sample1;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import com.poluhin.ss.demo.config.TestSecurityConfig;
import com.poluhin.ss.demo.service.ResourceObjectService;
import com.poluhin.ss.demo.utils.JwtTokenUtils;
import java.util.List;
import lombok.SneakyThrows;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@SpringBootTest
@AutoConfigureMockMvc
class ResourceControllerTest {
	private final static String TEST_USER = "test_user";
	private final static String PASSWORD = "test_user1";
	@Autowired
	private MockMvc mockMvc;
	@MockBean
	JwtTokenUtils jwtTokenUtils;
	@MockBean
	private ResourceObjectService service;
	@Value("classpath:output_resource_sample.json")
	private Resource outputSampleJson;
	@Test
	@SneakyThrows
	@WithMockUser(username = TEST_USER,password = PASSWORD,roles = {"USER"})
	void createResourceObjectLoginByUser_Success() {
		when(service.save(resourceObject_sample1)).thenReturn(1);

		MvcResult mvcResult = mockMvc.perform(
						post("/resource")
								.contentType(MediaType.APPLICATION_JSON)
								.content(getResourceAsString(outputSampleJson)))
				.andExpect(status().isOk())
				.andReturn();

		String result = mvcResult.getResponse().getContentAsString();

		assertNotNull(result);
		assertEquals(result, String.valueOf(1));
	}

	@Test
	@SneakyThrows
	void createResourceObjectAuthByJwtToken_Success() {
		final String ROLE_ADMIN = "ROLE_ADMIN";
		UserDetails userDetailsSample1 = new User(TEST_USER,
		                                          PASSWORD,
		                                          List.of(new SimpleGrantedAuthority(ROLE_ADMIN)));
		String token = TestSecurityConfig.createToken(userDetailsSample1);
		when(jwtTokenUtils.getUsername(any(String.class))).thenReturn(TEST_USER);
		when(jwtTokenUtils.getRoles(any(String.class))).thenReturn(List.of(ROLE_ADMIN));
		when(service.save(resourceObject_sample1)).thenReturn(1);

		MvcResult mvcResult = mockMvc.perform(
						post("/resource")
								.contentType(MediaType.APPLICATION_JSON)
								.content(getResourceAsString(outputSampleJson))
								.header("Authorization", "Bearer " + token))
				.andExpect(status().isOk())
				.andReturn();
		String result = mvcResult.getResponse().getContentAsString();

		assertNotNull(result);
		assertEquals(result, String.valueOf(1));
	}

	@Test
	@SneakyThrows
	void createResourceObject_UnSuccess() {
		when(service.save(resourceObject_sample1)).thenReturn(1);

		MvcResult mvcResult = mockMvc.perform(
						post("/resource")
								.contentType(MediaType.APPLICATION_JSON)
								.content(getResourceAsString(outputSampleJson)))
				.andExpect(status().isUnauthorized())
				.andReturn();

		String result = mvcResult.getResponse().getContentAsString();

		assertNotNull(result);
	}

	@Test
	@SneakyThrows
	@WithMockUser(username = TEST_USER,password = PASSWORD,roles = {"USER"})
	void getResourceObjectLoginByUser_Success() {
		when(service.get(1)).thenReturn(resourceObject_sample1);

		MvcResult mvcResult = mockMvc.perform(
						get("/resource/1")
								.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn();

		String result = mvcResult.getResponse().getContentAsString();

		assertNotNull(result);
		assertEquals(result, getResourceAsString(outputSampleJson));
	}

	@Test
	@SneakyThrows
	void getResourceObjectAuthByJwtToken_Success() {
		final String ROLE_ADMIN = "ROLE_ADMIN";
		UserDetails userDetailsSample1 = new User(TEST_USER,
		                                          PASSWORD,
		                                          List.of(new SimpleGrantedAuthority(ROLE_ADMIN)));
		String token = TestSecurityConfig.createToken(userDetailsSample1);
		when(jwtTokenUtils.getUsername(any(String.class))).thenReturn(TEST_USER);
		when(jwtTokenUtils.getRoles(any(String.class))).thenReturn(List.of(ROLE_ADMIN));
		when(service.get(1)).thenReturn(resourceObject_sample1);

		MvcResult mvcResult = mockMvc.perform(
						get("/resource/1")
								.contentType(MediaType.APPLICATION_JSON)
								.header("Authorization", "Bearer " + token))
				.andExpect(status().isOk())
				.andReturn();
		String result = mvcResult.getResponse().getContentAsString();

		assertNotNull(result);
		assertEquals(result, getResourceAsString(outputSampleJson));
	}
	@Test
	@SneakyThrows
	void getResourceObject_UnSuccess() {
		when(service.get(1)).thenReturn(resourceObject_sample1);

		MvcResult mvcResult = mockMvc.perform(
						get("/resource/1")
								.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isUnauthorized())
				.andReturn();

		String result = mvcResult.getResponse().getContentAsString();

		assertNotNull(result);
	}

	@SneakyThrows
	private String getResourceAsString(Resource resource) {
		return IOUtils.toString(resource.getInputStream());
	}
}