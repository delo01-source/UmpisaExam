package com.exam.clientbff.controller;

import java.util.Optional;
import java.util.WeakHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.exam.clientbff.domain.Customer;
import com.exam.clientbff.model.customer.CustomerRequest;
import com.exam.clientbff.model.customer.GetCustomerRequest;
import com.exam.clientbff.model.customer.UpdateCustomerRequest;
import com.exam.clientbff.service.CustomerService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Schema;

@RestController
@RequestMapping("/api/v1/customer")
public class CustomerController {

	@Autowired
	CustomerService customerService;

	private final Logger LOGGER = LoggerFactory.getLogger(getClass());

	//creates a customer supplied Customer Request form
	@Operation(summary = "Create Customer")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Customer Created", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = Customer.class)) }),
			@ApiResponse(responseCode = "400", description = "Invalid information supplied", content = @Content) })
	@PostMapping("/createCustomer")
	public ResponseEntity<Object> createCustomer(@RequestBody CustomerRequest request) {
		LOGGER.info("START CREATE User..");
		Customer customer = null;

		Optional<Customer> existingCustomer = customerService.checkIfCustomerExists(request);
		if (existingCustomer.isEmpty()) {
			//Create new customer
			customer = customerService.customerRequestToEnitity(request);
			customer = customerService.createCustomer(customer);
		} else {
			//Customer Exists return customer info with id for reservation creation/ for form fill-up
			return ResponseEntity.ok().body(existingCustomer);
		}

		if (customer == null)
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to insert orders.");

		LOGGER.info("END CREATE CUSTOMER..");
		return ResponseEntity.ok().body(customer);
	}
	
	
	//Updates the customer assuming that the ID is given
	@Operation(summary = "Update Customer")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Customer Updated", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = Customer.class)) }),
			@ApiResponse(responseCode = "400", description = "Customer not found", content = @Content),
			@ApiResponse(responseCode = "404", description = "Customer not found", content = @Content) })
	@PostMapping("/updateCustomer")
	public ResponseEntity<Object> updateCustomer(@RequestBody UpdateCustomerRequest request) {
		LOGGER.info("START UPDATE CUSTOMER..");
		
		Customer customer;
		Optional<Customer> temp = customerService.findCustomerById(request.getCustomerId());
		if (!temp.isPresent()) {
			//set updated values for all the fields, even though only the notif mode is required.
			customer = new Customer();
			customer.setNotificationMode(request.getNotificationMode());
			customer.setEmail(request.getEmail());
			customer.setName(request.getName());
			customer.setPhoneNumber(request.getPhoneNumber());
			customerService.updateCustomer(customer);
			
		} else {
			WeakHashMap<String, String> response = new WeakHashMap<>();
			response.put("message", "Invalid Request.");
			return ResponseEntity.badRequest().body(response);
		}

		LOGGER.info("END UPDATE CUSTOMER..");
		return ResponseEntity.ok().body(customer);
	}
	
	//Finds the customer given either phone number or email, used for when retrieving customer reservations
	@Operation(summary = "Find Customer by Email or Phone")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Customer Found", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = Customer.class)) }),
			@ApiResponse(responseCode = "400", description = "Customer not found", content = @Content),
			@ApiResponse(responseCode = "404", description = "Customer not found", content = @Content) })
	@PostMapping("/findCustomer")
	public ResponseEntity<Object> findCustomer(@RequestBody GetCustomerRequest request) {
		LOGGER.info("START FIND CUSTOMER..");
		Optional<Customer> customer;
		customer = customerService.findCustomerByEmailOrPhone(request);
		System.out.println(customer.toString());
		if (customer.isEmpty()) {
			WeakHashMap<String, String> response = new WeakHashMap<>();
			response.put("message", "Invalid Request.(Customer does not exists)");
			return ResponseEntity.badRequest().body(response);
		} 
		LOGGER.info("END FIND CUSTOMER..");
		return ResponseEntity.ok().body(customer);
	}
	
	//Finds the customer given either phone number or email, used for when retrieving customer reservations
	@Operation(summary = "Find Customer by ID")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Customer Found", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = Customer.class)) }),
			@ApiResponse(responseCode = "400", description = "Customer not found", content = @Content),
			@ApiResponse(responseCode = "404", description = "Customer not found", content = @Content) })
	@GetMapping("/getCustomer/{id}")
	public ResponseEntity<Object> getCustomerByID(@PathVariable Long id) {
		LOGGER.info("START FIND CUSTOMER..");
		Optional<Customer> customer;
		customer = customerService.findCustomerById(id);
		if (customer.isEmpty()) {
			WeakHashMap<String, String> response = new WeakHashMap<>();
			response.put("message", "Invalid Request.(Customer does not exists)");
			return ResponseEntity.badRequest().body(response);
		} 
		LOGGER.info("END FIND CUSTOMER..");
		return ResponseEntity.ok().body(customer);
	}

}
