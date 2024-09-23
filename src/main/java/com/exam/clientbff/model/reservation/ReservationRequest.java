package com.exam.clientbff.model.reservation;

import java.time.Instant;

import lombok.Data;

@Data
public class ReservationRequest {

	private Instant reservationDate;

	private int numOfGuests;

	private long customerId;

}
