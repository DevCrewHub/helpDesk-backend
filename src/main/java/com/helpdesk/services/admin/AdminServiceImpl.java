package com.helpdesk.services.admin;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.helpdesk.dto.UserDto;
import com.helpdesk.entities.User;
import com.helpdesk.enums.UserRole;
import com.helpdesk.repositories.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

	private final UserRepository userRepository;
	
	@Override
	public List<UserDto> getUsers() {
	    return userRepository.findAll()
	        .stream()
	        .filter(user -> {
	            UserRole role = user.getUserRole();
	            return role == UserRole.CUSTOMER || role == UserRole.AGENT;
	        })
	        .map(User::getUserDto)
	        .collect(Collectors.toList());
	}
	
}