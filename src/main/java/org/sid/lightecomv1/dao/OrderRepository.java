package org.sid.lightecomv1.dao;

import javax.transaction.Transactional;

import org.sid.lightecomv1.entities.Order;
import org.sid.lightecomv1.entities.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin("*")
@RepositoryRestResource
public interface OrderRepository extends JpaRepository<Order, Long> {
@Modifying
@Transactional
	@Query("update Order o set o.payment=:x where o.id=:y")
	public void setOrder(@Param("x") Payment payment, @Param("y") Long id);
}
