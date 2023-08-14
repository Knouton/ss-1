package com.poluhin.ss.demo.service;

import com.poluhin.ss.demo.domain.entity.RoleEntity;
import com.poluhin.ss.demo.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleService {

	private final RoleRepository roleRepository;

	public RoleEntity findRoleByName(String name) {
		return roleRepository.findByName(name).orElseThrow(
				() -> new SecurityException(String.format("RoleEntity with name '%s' not found", name)));
	}

}
