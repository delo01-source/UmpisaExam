package com.exam.clientbff;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.repository.CrudRepository;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;

import com.exam.clientbff.domain.Customer;
import com.exam.clientbff.model.CreateCustomerAndReservationRequest;
import com.exam.clientbff.repository.CustomerRepository;
import com.exam.clientbff.service.ClientBffService;
import com.exam.clientbff.util.Constants;
import com.exam.clientbff.util.Constants.Contact;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;

@EnableScheduling
@SpringBootApplication
public class ClientBffApplication {

	public static void main(String[] args) {

		SpringApplication.run(ClientBffApplication.class, args);
	}

	@Bean
	public CommandLineRunner demo(ClientBffService service) {
		return (args) -> {
			// save a few customers and reservations
			service.createUserAndReservation(new CreateCustomerAndReservationRequest(Contact.SMS, "test name 1",
					"test1@test.com", "0980982", Instant.now().plus(Duration.ofHours(2)), 10, 0));
			service.createUserAndReservation(new CreateCustomerAndReservationRequest(Contact.SMS, "test name 2",
					"test2@test.com", "0980983", Instant.now().plus(Duration.ofHours(2)), 10, 0));
			service.createUserAndReservation(new CreateCustomerAndReservationRequest(Contact.SMS, "test name 3",
					"test3@test.com", "0980984", Instant.now().plus(Duration.ofHours(2)), 10, 0));
			service.createUserAndReservation(new CreateCustomerAndReservationRequest(Contact.SMS, "test name 4",
					"test4@test.com", "0980985", Instant.now().plus(Duration.ofHours(2)), 10, 0));
			
			//create multiple reservations for a single user
			service.createUserAndReservation(new CreateCustomerAndReservationRequest(Contact.SMS, "test name 5",
					"test1@test.com", "098098", Instant.now().plus(Duration.ofHours(2)), 10, 0));
			service.createUserAndReservation(new CreateCustomerAndReservationRequest(Contact.SMS, "test name 5",
					"test1@test.com", "098098", Instant.now().plus(Duration.ofHours(2)), 10, 0));
			service.createUserAndReservation(new CreateCustomerAndReservationRequest(Contact.SMS, "test name 5",
					"test1@test.com", "098098", Instant.now().plus(Duration.ofHours(2)), 10, 0));


		};
	}

}
