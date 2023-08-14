package com.poluhin.ss.demo.service;

import com.poluhin.ss.demo.domain.entity.UserEntity;
import com.poluhin.ss.demo.domain.model.RegistrationUser;
import com.poluhin.ss.demo.repository.UserRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

	private final UserRepository userRepository;
	private final RoleService roleService;
	private final PasswordEncoder passwordEncoder;


	private static final String EXCEPTION_USER_FORMAT = "UserEntity with name: '%s' not found";
	private static final String DEFAULT_ROLE = "ROLE_USER";

	@Override
	@Transactional
	public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
		UserEntity userEntity = findByUsername(username).orElseThrow(
				() -> new UsernameNotFoundException(String.format(EXCEPTION_USER_FORMAT, username)));


		return new org.springframework.security.core.userdetails.User(
				userEntity.getName(),
				userEntity.getPassword(),
				userEntity.getRoleEntities().stream()
						.map(role -> new SimpleGrantedAuthority(role.getName()))
						.collect(Collectors.toList())
		);
	}
	public Optional<UserEntity> findByUsername(String username) {
		return userRepository.findByName(username);
	}

	public UserEntity createNewUser(RegistrationUser registrationUserDto) {
		UserEntity user = new UserEntity();
		user.setName(registrationUserDto.getUsername());
		user.setPassword(passwordEncoder.encode(registrationUserDto.getPassword()));
		user.setRoleEntities(List.of(roleService.findRoleByName(DEFAULT_ROLE)));
		return userRepository.save(user);
	}

}
