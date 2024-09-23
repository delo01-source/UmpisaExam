package com.exam.clientbff.service;

import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.exam.clientbff.domain.Customer;
import com.exam.clientbff.model.customer.CustomerRequest;
import com.exam.clientbff.model.customer.GetCustomerRequest;
import com.exam.clientbff.repository.CustomerRepository;
import com.exam.clientbff.util.Constants.Contact;

@Service
public class CustomerService {
	
	private CustomerRepository customerRepository;
	
	public CustomerService(CustomerRepository customerRepository) {
		this.customerRepository = customerRepository;
	}

	// creates a customer
	public Customer createCustomer(Customer customer) {

		customerRepository.save(customer);
		return customer;

	}

	// Update a customer
	public Customer updateCustomer(Customer customer) {

		customerRepository.save(customer);
		return customer;

	}

	// converts customer request to entity
	public Customer customerRequestToEnitity(CustomerRequest request) {
		Customer customer = new Customer();
		customer.setName(request.getName());
		customer.setPhoneNumber(request.getPhoneNumber());
		customer.setNotificationMode(request.getNotificationMode()); // drop-down or radio button
		customer.setEmail(request.getEmail());

		return customer;

	}

	// change customer notif mode via customer ID, customer in session.
	public void customerChangeNotificationMode(long customerId, Contact mode) {
		Customer customer = customerRepository.findById(customerId);
		customer.setNotificationMode(mode);
		customerRepository.save(customer);
	}



	// finds customer by Id
	public Optional<Customer> findCustomerById(Long id) {
		return Optional.of(customerRepository.findById(id))
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
						String.format("User with id %d not found", id)));

	}

	// checks if customer exists via email or phone and returns existing customer
	public Optional<Customer> findCustomerByEmailOrPhone(GetCustomerRequest customerReq) {
		if(this.customerRepository.findByPhoneNumber(customerReq.getPhoneNumber()).isEmpty()){
			return this.customerRepository.findByEmail(customerReq.getEmail());
		}
		else
			return this.customerRepository.findByPhoneNumber(customerReq.getPhoneNumber());
	

	}

	
	// check if customer exists and returns boolean
	public Optional<Customer> checkIfCustomerExists(CustomerRequest request) {
		GetCustomerRequest findReq = new GetCustomerRequest();
		findReq.setEmail(request.getEmail());
		findReq.setPhoneNumber(request.getPhoneNumber());
			
		return Optional.ofNullable(findCustomerByEmailOrPhone(findReq)).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
				"Customer not found"));
		

	}
}
