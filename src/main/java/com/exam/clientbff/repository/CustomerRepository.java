package com.exam.clientbff.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.exam.clientbff.domain.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

	Customer findById(long id);

	Customer findByName(String name);
	@Query(value ="SELECT * FROM customer WHERE phone_number = ?1", nativeQuery = true)
	Optional<Customer> findByPhoneNumber(String phoneNumber);
	
	@Query(value ="SELECT * FROM customer WHERE email = ?1", nativeQuery = true)
	Optional<Customer> findByEmail(@Param("email") String email);
}