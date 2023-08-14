package com.poluhin.ss.demo.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import com.poluhin.ss.demo.domain.entity.RoleEntity;
import com.poluhin.ss.demo.repository.RoleRepository;
import java.util.Optional;
import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RoleServiceTest {
	private static final String ROLE_USER = "ROLE_USER";
	@Mock
	private RoleRepository repository;
	@InjectMocks
	private RoleService service;

	private RoleEntity roleEntity;

	@BeforeEach
	void init() {
		roleEntity = new RoleEntity(1, ROLE_USER);
	}

	@Test
	void findRoleByName_Success() {
		when(repository.findByName(ROLE_USER)).thenReturn(Optional.of(roleEntity));

		val result = service.findRoleByName(ROLE_USER);

		assertEquals(result, roleEntity);
	}

}