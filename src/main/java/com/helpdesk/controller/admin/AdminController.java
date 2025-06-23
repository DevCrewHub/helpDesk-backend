package com.helpdesk.controller.admin;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.helpdesk.services.admin.AdminService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
public class AdminController {
	
	private final AdminService adminService;
	
	@GetMapping("/users")
    public ResponseEntity<?> getUsers() {
//        log.info("Admin requested user list.");
        return ResponseEntity.ok(adminService.getUsers());
    }

}
