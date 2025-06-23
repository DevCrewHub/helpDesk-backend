package com.helpdesk.dto;

import com.helpdesk.enums.UserRole;

import lombok.Data;

@Data
public class SignupRequest {
	
	private String userName;
	
	private String email;
	
	private String password;
	
	private String fullName;
	
	private String phoneNumber;
	
	private String userRole;
	
	private String departmentName;

}
