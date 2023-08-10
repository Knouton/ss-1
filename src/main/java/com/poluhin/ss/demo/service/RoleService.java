package com.poluhin.ss.demo.service;

import com.poluhin.ss.demo.domain.entity.Role;
import com.poluhin.ss.demo.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleService {

	private final RoleRepository roleRepository;

	public Role findRoleByName(String name) {
		//TODO change Exception
		return roleRepository.findByName(name).orElseThrow(
				() -> new SecurityException(String.format("Role with name '%s' not found", name)));
	}

}
