package com.exam.clientbff.service;

import java.time.Instant;


import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import com.exam.clientbff.model.CreateCustomerAndReservationRequest;
import com.exam.clientbff.model.customer.CustomerRequest;
import com.exam.clientbff.model.reservation.ReservationRequest;
import com.exam.clientbff.util.InstantDeserializer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;


//Sample Client bff for when creating a Customer and Reservation calling both Customer and Reservation Microservice
@Service
public class ClientBffService {

	//can be in config
	final String customerUri = "http://localhost:8080/api/v1/customer";
	final String reservationUri = "http://localhost:8080/api/v1/reservation";
	
	Gson gson = new GsonBuilder()
            .registerTypeAdapter(Instant.class, new InstantDeserializer())
            .create();

	public ResponseEntity<String> createUserAndReservation(CreateCustomerAndReservationRequest request) {
		UriComponents uriComponents = UriComponentsBuilder.fromHttpUrl(this.customerUri + "/createCustomer").build();
		String urlCustomer = uriComponents.toString();
		uriComponents = UriComponentsBuilder.fromHttpUrl(this.reservationUri + "/createReservation").build();
		String urlReservation = uriComponents.toString();
		RestTemplate restTemplate = new RestTemplate();
		CustomerRequest customerReq = new CustomerRequest();
		
		customerReq.setName(request.getName());
		customerReq.setNotificationMode(request.getNotificationMode());
		customerReq.setPhoneNumber(request.getPhoneNumber());
		customerReq.setEmail(request.getEmail());

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> entity = new HttpEntity<String>(gson.toJson(customerReq), headers);
		System.out.println(entity.toString());
		ResponseEntity<String> responseEntity = restTemplate.exchange(urlCustomer, HttpMethod.POST, entity,
				String.class);

		HttpStatusCode statusCode = responseEntity.getStatusCode();
		if (statusCode == HttpStatus.OK) {
			ReservationRequest reservationReq = new ReservationRequest();

			reservationReq.setCustomerId(gson.fromJson(responseEntity.getBody(), JsonObject.class).get("id").getAsLong());
			reservationReq.setNumOfGuests(request.getNumOfGuests());
			reservationReq.setReservationDate(request.getReservationDate());
			System.out.println("test" + reservationReq.toString());
			entity = new HttpEntity<String>(gson.toJson(reservationReq), headers);
			responseEntity = restTemplate.exchange(urlReservation, HttpMethod.POST, entity, String.class);
		}
		return responseEntity;

	}

}
