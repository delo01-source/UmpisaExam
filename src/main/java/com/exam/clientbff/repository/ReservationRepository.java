package com.exam.clientbff.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.exam.clientbff.domain.Reservation;


@Repository
public interface ReservationRepository extends CrudRepository<Reservation, Long> {
	
	Reservation findById(long id);
	
	@Query(value ="SELECT * FROM reservation WHERE remarks not like 'CANCELLED' and customer_id = ?1", nativeQuery = true)
	List<Reservation> findByCustomerId(long id);

	@Query(value ="SELECT * FROM reservation where DATEDIFF('HOUR', NOW(), reservation_date) <= 4 and (remarks <> 'REMINDED' and remarks <> 'CANCELLED')", nativeQuery = true)
	ArrayList<Reservation> findAllToNotify();

}