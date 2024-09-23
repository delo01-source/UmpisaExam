package com.exam.clientbff.service;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import com.exam.clientbff.domain.Reservation;
import com.exam.clientbff.model.reservation.ReservationRequest;
import com.exam.clientbff.model.reservation.UpdateReservationRequest;
import com.exam.clientbff.repository.ReservationRepository;
import com.exam.clientbff.util.NotificationScheduler;

@Service
public class ReservationService {
	
	private ReservationRepository reserveationRepository;

	
	public ReservationService(ReservationRepository reserveationRepository) {
		this.reserveationRepository = reserveationRepository;
	}

	public Reservation createReservation(Reservation reservation) {
		reserveationRepository.save(reservation);
		return reservation;

	}
	
	public List<Reservation> getReservationsByCustomerId(long customerId) {
		
		return reserveationRepository.findByCustomerId(customerId);

	}
	//converts reservation request to entity
	public Reservation reservationRequestToEnitity(ReservationRequest request) {
		Reservation reservation = new Reservation();
		reservation.setNumOfGuests(request.getNumOfGuests());
		reservation.setReservationDate(request.getReservationDate());
		reservation.setCustomerId(request.getCustomerId());
		reservation.setRemarks("CREATED");
		return reservation;

	}
	//checks if reservation exists
	public Boolean checkIfReservationExists(UpdateReservationRequest request) {
		Optional<Reservation> temp = Optional.ofNullable(this.reserveationRepository.findById(request.getReservationId()));
		return(!temp.isEmpty());
	}
	
	//Update Reservation by Id
	public Reservation updateReservation(UpdateReservationRequest request) {
		Reservation reservation= reserveationRepository.findById(request.getReservationId());
		reservation.setNumOfGuests(request.getNumOfGuests());
		reservation.setReservationDate(request.getReservationDate());
		reservation.setReservationId(request.getReservationId());
		reservation.setRemarks("UPDATED");
		this.reserveationRepository.save(reservation);
		return reservation;

	}
	//Mark reservation as cancelled
	public Reservation cancelReservation(long id) {
		Reservation reservation = this.reserveationRepository.findById(id);
		reservation.setNumOfGuests(reservation.getNumOfGuests());
		reservation.setReservationDate(reservation.getReservationDate());
		reservation.setRemarks("CANCELLED");
		this.reserveationRepository.save(reservation);
		return reservation;

	}
	

	//Mark reservation as cancelled
	public Optional<List<Reservation>> getReservationsByUserId(long id) {

		return Optional.ofNullable(this.reserveationRepository.findByCustomerId(id));

	}
	//Validates if reservation is 1 hour after now
	public Boolean validateReservationRequest(Instant instant) {
		Boolean isValid = true;
		if (instant.isBefore(Instant.now().plus(Duration.ofHours(1)))) {

			isValid = false;
		}

		return isValid;
	}
	
	
	//For the reservation reminder
	public ArrayList<Reservation> notifyReservation(){
		ArrayList<Reservation> reservationList =this.reserveationRepository.findAllToNotify();
		
		return reservationList;
	}

	public Optional<Reservation> getReservationsById(Long id) {
		return this.reserveationRepository.findById(id);

	}
	
	
}
