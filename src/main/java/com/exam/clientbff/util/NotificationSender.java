package com.exam.clientbff.util;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.exam.clientbff.domain.Customer;
import com.exam.clientbff.service.CustomerService;
import com.exam.clientbff.util.Constants.Contact;

@Component
public class NotificationSender {

	private CustomerService customerService;



	private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    public NotificationSender(CustomerService customerService) {
        this.customerService = customerService;
    }

	public void sendNotification(Long customerId, String remarks) {
		Optional<Customer> customer = this.customerService.findCustomerById(customerId);
		if (customer.isPresent()) {
			if (customer.get().getNotificationMode().equals(Contact.EMAIL)) {
				sendEmail(customer.get().getEmail(), remarks);
			} else
				sendSMS(customer.get().getPhoneNumber(), remarks);
		}

	}

	public void sendEmail(String email, String remarks) {
		LOGGER.info("Emailed {}" , message(remarks));

	}

	public void sendSMS(String phoneNumber, String remarks) {
		LOGGER.info("SMS sent {}", message(remarks));
	}

	public String message(String remarks) {
		switch (remarks) {
		case "CREATED":
			return "reservation created";

		case "UPDATED":
			return "reservation updated";

		case "CANCELLED":
			return "reservation cancelled";

		case "REMINDER":
			return "reservation reminder";

		}
		return null;

	}

}
