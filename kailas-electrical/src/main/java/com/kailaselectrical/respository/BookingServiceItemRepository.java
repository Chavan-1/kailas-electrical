package com.kailaselectrical.respository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kailaselectrical.entity.BookingServiceItem;

public interface BookingServiceItemRepository extends JpaRepository<BookingServiceItem, Long>{

}
