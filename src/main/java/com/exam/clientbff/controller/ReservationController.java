package com.exam.clientbff.controller;

import java.util.List;
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
import com.exam.clientbff.domain.Reservation;
import com.exam.clientbff.model.reservation.ReservationRequest;
import com.exam.clientbff.model.reservation.UpdateReservationRequest;
import com.exam.clientbff.service.ReservationService;
import com.exam.clientbff.util.NotificationScheduler;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Schema;

@RestController
@RequestMapping("/api/v1/reservation")
public class ReservationController {

	@Autowired
	ReservationService reservationService;
	
	@Autowired 
	NotificationScheduler notif;

	private final Logger LOGGER = LoggerFactory.getLogger(getClass());

	
	//Create Reservation 
	@Operation(summary = "Create Reservation")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Reservation created", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = Reservation.class)) }),
			@ApiResponse(responseCode = "400", description = "Invalid input", content = @Content),
			@ApiResponse(responseCode = "500", description = "Invalid request", content = @Content)})
	@PostMapping("/createReservation")
	public ResponseEntity<Object> createReservation(@RequestBody ReservationRequest request) {
		LOGGER.info("START CREATE RESERVATION..");
		Reservation reservation = null;
		//Validates if time 1 hour after now
		System.out.println(request.toString());
		Boolean isValidRequest = reservationService.validateReservationRequest(request.getReservationDate());
		if (isValidRequest) {
			reservation = reservationService.reservationRequestToEnitity(request);
			reservation = reservationService.createReservation(reservation);
			notif.sendNotification(reservation.getCustomerId(), reservation.getRemarks());
		} else {
			WeakHashMap<String, String> response = new WeakHashMap<>();
			response.put("message", "Invalid Request.");
			return ResponseEntity.badRequest().body(response);
		}

		if (reservation == null)
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to create reservation.");

		LOGGER.info("END CREATE RESERVATION..");
		return ResponseEntity.ok().body(reservation);
	}
	
	//Create Reservation 
	@Operation(summary = "Update Reservation")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Reservation updated", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = Reservation.class)) }),
			@ApiResponse(responseCode = "400", description = "Invalid input", content = @Content),
			@ApiResponse(responseCode = "404", description = "reservation not found", content = @Content) })
	@PostMapping("/updateReservation")
	public ResponseEntity<Object> updateReservation(@RequestBody UpdateReservationRequest request) {
		LOGGER.info("START UPDATE RESERVATION..");
		
		Reservation reservation = null;
		//Validates if time 1 hour after now and exists
		Boolean isValidRequest = reservationService.validateReservationRequest(request.getReservationDate()) && this.reservationService.checkIfReservationExists(request);
		if (isValidRequest ) {
		reservation = reservationService.updateReservation(request);
		} else {
			WeakHashMap<String, String> response = new WeakHashMap<>();
			response.put("message", "Invalid Request.");
			return ResponseEntity.badRequest().body(response);
		}

		if (reservation == null)
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to update reservation.");
		notif.sendNotification(reservation.getCustomerId(), reservation.getRemarks());
		LOGGER.info("END UPDATE RESERVATION..");

		return ResponseEntity.ok().body(reservation);
	}
	
	//Finds the customer given either phone number or email, used for when retrieving customer reservations
	@Operation(summary = "Find reservations by customer ID")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Reservations found", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = Customer.class)) }),
			@ApiResponse(responseCode = "400", description = "Reservations not found", content = @Content),
			@ApiResponse(responseCode = "404", description = "Reservations not found", content = @Content) })
	@GetMapping("/getReservations/{id}")
	public ResponseEntity<Object> getReservationsByCustomerId(@PathVariable Long id) {
		LOGGER.info("START FIND RESERVATIONS..");
		Optional<List<Reservation>> reservations = this.reservationService.getReservationsByUserId(id);
		if (reservations.isEmpty()) {
			WeakHashMap<String, String> response = new WeakHashMap<>();
			response.put("message", "Invalid Request.(No reservations found)");
			return ResponseEntity.badRequest().body(response);
		} 
		LOGGER.info("END FIND RESERVATIONS..");
		return ResponseEntity.ok().body(reservations);
	}
	
	//Finds the customer given either phone number or email, used for when retrieving customer reservations
	@Operation(summary = "Delete reservation by ID")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Reservations found", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = Customer.class)) }),
			@ApiResponse(responseCode = "400", description = "Reservations not found", content = @Content),
			@ApiResponse(responseCode = "404", description = "Reservations not found", content = @Content) })
	@PostMapping("/deleteReservation/{id}")
	public ResponseEntity<Object> deleteReservationById(@PathVariable Long id) {
		LOGGER.info("START DELETE RESERVATION..");
		Optional<Reservation> reservation = this.reservationService.getReservationsById(id);
		if (reservation.isEmpty()) {
			WeakHashMap<String, String> response = new WeakHashMap<>();
			response.put("message", "Invalid Request.(No reservations found)");
			return ResponseEntity.badRequest().body(response);
		} 
		reservation = Optional.of(this.reservationService.cancelReservation(id));
		LOGGER.info("END DELETE RESERVATION..");
		notif.sendNotification(reservation.get().getCustomerId(), reservation.get().getRemarks());
		return ResponseEntity.ok().body(reservation);
	}

}
