package com.exam.clientbff.util;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.exam.clientbff.domain.Customer;
import com.exam.clientbff.repository.ReservationRepository;
import com.exam.clientbff.service.CustomerService;
import com.exam.clientbff.service.ReservationService;
import com.exam.clientbff.util.Constants.Contact;

@Component
public class NotificationScheduler {


	private final ReservationService reservationService;
	
	private final NotificationSender sender;

	private final ReservationRepository reservationRepo;

	public NotificationScheduler(ReservationService reservationService, 
			ReservationRepository reservationRepo, NotificationSender sender) {
		this.reservationService = reservationService;
		this.reservationRepo = reservationRepo;
		this.sender = sender;
	}



	@Scheduled(cron = "0 0/10 * * * *") // checks, sends notifications, and updates reservations within 4 hours of now
										// every 10 mins
	public void run() {
		reservationService.notifyReservation().forEach(reservation -> {
			sender.sendNotification(reservation.getCustomerId(), "REMINDER");
			reservation.setRemarks("REMINDED");
			reservationRepo.save(reservation);
		});

	}



}
