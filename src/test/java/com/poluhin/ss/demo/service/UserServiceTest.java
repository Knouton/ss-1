package com.poluhin.ss.demo.service;

import static com.poluhin.ss.demo.sample.RegistrationUserSample.registrationUser_sample1;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import com.poluhin.ss.demo.domain.entity.ResourceObjectEntity;
import com.poluhin.ss.demo.domain.entity.RoleEntity;
import com.poluhin.ss.demo.domain.entity.UserEntity;
import com.poluhin.ss.demo.domain.model.RegistrationUser;
import com.poluhin.ss.demo.repository.UserRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

	@Mock
	private UserRepository repository;
	@Mock
	private RoleService roleService;
	@Mock
	private PasswordEncoder passwordEncoder;

	@InjectMocks
	UserService service;

	private UserDetails user;
	private UserEntity userEntity;
	private RoleEntity roleEntity;
	@BeforeEach
	void init(){
		user = new org.springframework.security.core.userdetails.User(
				"test_user",
				"test_user1",
				List.of(new SimpleGrantedAuthority("ROLE_USER")));
		roleEntity = new RoleEntity(1,"ROLE_USER");
		userEntity = new UserEntity(1L,
		                            "test_user",
		                            "test_user1",
		                            List.of(roleEntity));
	}

	@Test
	void loadUserByUsername_Success() {
		when(repository.findByName(user.getUsername())).thenReturn(Optional.of(userEntity));

		val result = service.loadUserByUsername(user.getUsername());

		assertEquals(result, user);
	}

	@Test
	void createNewUser_Success() {
		when(repository.save(Mockito.any(UserEntity.class))).thenAnswer(i -> i.getArguments()[0]);
		when(roleService.findRoleByName("ROLE_USER")).thenReturn(roleEntity);

		val result = service.createNewUser(registrationUser_sample1);

		assertEquals(result.getName(), userEntity.getName());
		assertEquals(result.getRoleEntities(), userEntity.getRoleEntities());
	}
}