package com.exam.clientbff.model.customer;

import com.exam.clientbff.util.Constants;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class CustomerRequest {

	private Constants.Contact notificationMode;

	private String name;
	
	@Email(message = "Email is not valid")
	@Pattern(message = "Email is not valid", regexp = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$")
	private String email;
	
	private String phoneNumber;

}
