package com.example.demo.controller.role;

import com.example.demo.common.Endpoints;
import com.example.demo.model.entity.role.Role;
import com.example.demo.service.role.RoleService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;

    @GetMapping(Endpoints.URL_ROLE)
    public ResponseEntity<List<Role>> getAllRoles() {
        List<Role> roles = roleService.getAllRoles();
        if (CollectionUtils.isEmpty(roles)) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(roles, HttpStatus.OK);
    }
}
