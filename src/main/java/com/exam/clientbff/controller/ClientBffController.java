package com.exam.clientbff.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import com.exam.clientbff.domain.Customer;
import com.exam.clientbff.model.CreateCustomerAndReservationRequest;
import com.exam.clientbff.service.ClientBffService;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Schema;

@RestController
@RequestMapping("/api/v1/customer")
public class ClientBffController {

	@Autowired
	ClientBffService clientBffService;

	private final Logger LOGGER = LoggerFactory.getLogger(getClass());

	//creates a customer supplied Customer Request form
	@Operation(summary = "Create Customer")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Customer Created", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = Customer.class)) }),
			@ApiResponse(responseCode = "400", description = "Invalid information supplied", content = @Content) })
	@PostMapping("/createReservation")
	public ResponseEntity<String> createReservation(@RequestBody CreateCustomerAndReservationRequest request) {
		LOGGER.info("START CREATE RESERVATION..");
		ResponseEntity<String> response = this.clientBffService.createUserAndReservation(request);
		LOGGER.info("END CREATE RESERVATION..");
		return response;




	}
	


}
