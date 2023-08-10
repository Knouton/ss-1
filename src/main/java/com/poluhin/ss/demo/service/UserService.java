package com.poluhin.ss.demo.service;

import com.poluhin.ss.demo.domain.entity.User;
import com.poluhin.ss.demo.repository.UserRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

	private final UserRepository userRepository;
	private final RoleService roleService;

	private static final String EXCEPTION_USER_FORMAT = "User with name: '%s' not found";
	private static final String DEFAULT_ROLE = "ROLE_USER";

	public void createNewUser(User user) {
		user.setRoles(List.of(roleService.findRoleByName(DEFAULT_ROLE)));
		userRepository.save(user);
	}

	@Override
	@Transactional
	public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
		User user = findByUsername(username).orElseThrow(
				() -> new UsernameNotFoundException(String.format(EXCEPTION_USER_FORMAT, username)));


		return new org.springframework.security.core.userdetails.User(
				user.getName(),
				user.getPassword(),
				user.getRoles().stream()
						.map(role -> new SimpleGrantedAuthority(role.getName()))
						.collect(Collectors.toList())
		);
	}

	private Optional<User> findByUsername(String name) {
		return userRepository.findByName(name);
	}


}
