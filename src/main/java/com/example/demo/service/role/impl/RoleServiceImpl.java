package com.example.demo.service.role.impl;

import com.example.demo.model.entity.role.Role;
import com.example.demo.repository.role.RoleRepository;
import com.example.demo.service.role.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Override
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }
}
