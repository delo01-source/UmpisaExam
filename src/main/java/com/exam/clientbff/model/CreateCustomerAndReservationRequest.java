package com.exam.clientbff.model;

import java.time.Instant;
import com.exam.clientbff.util.Constants;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreateCustomerAndReservationRequest {

	private Constants.Contact notificationMode;

	private String name;

	@Email(message = "Email is not valid")
	@Pattern(message = "Email is not valid", regexp = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$")
	private String email;
	
	private String phoneNumber;

	private Instant reservationDate;

	private int numOfGuests;

	@Hidden
	private long customerId;

}
