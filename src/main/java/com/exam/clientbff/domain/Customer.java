package com.exam.clientbff.domain;

import org.springframework.beans.factory.annotation.Autowired;

import com.exam.clientbff.util.Constants;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Customer {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Autowired(required = true)
	@Enumerated(EnumType.STRING)
	private Constants.Contact notificationMode;

	@Autowired(required = true)
	private String name;
	

	private String email;
	private String phoneNumber;
	private String updatedBy;
	private String createdBy;
	private String dateCreated;
	private String dateUpdated;
	private String remarks;

}
