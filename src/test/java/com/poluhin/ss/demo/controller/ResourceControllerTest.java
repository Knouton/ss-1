package com.poluhin.ss.demo.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import com.poluhin.ss.demo.service.ResourceObjectService;
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
class ResourceControllerTest {

	@Autowired
	private MockMvc mockMvc;

	private ResourceObjectService service;

	@Test
	@SneakyThrows
	@WithMockUser(username = "test_user",password = "test_user1",roles = {"USER"})
	void getResourceObject() {
		//when(service.get(1)).thenReturn()

		MvcResult mvcResult = mockMvc.perform(
						get("/resource")
								.content(String.valueOf(MediaType.APPLICATION_JSON)))
				.andExpect(status().is4xxClientError())
				.andReturn();

		String result = mvcResult.getResponse().getContentAsString();

		assertNotNull(result);
	}





}