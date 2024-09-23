package com.exam.clientbff.model.reservation;

import java.time.Instant;

import lombok.Data;

@Data
public class UpdateReservationRequest {
	
	private long reservationId;

	private Instant reservationDate;

	private int numOfGuests;


}
