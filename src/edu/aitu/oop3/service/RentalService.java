
package edu.aitu.oop3.service;

import edu.aitu.oop3.entity.Rental;

import java.time.LocalDate;

public interface RentalService {
    Rental createRental(long customerId, long carId, LocalDate startDate, LocalDate endDate);
    int closeRental(long rentalId, String paymentMethod);
}