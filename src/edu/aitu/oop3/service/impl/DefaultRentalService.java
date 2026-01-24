
package edu.aitu.oop3.service.impl;

import edu.aitu.oop3.entity.Car;
import edu.aitu.oop3.entity.Payment;
import edu.aitu.oop3.entity.Rental;
import edu.aitu.oop3.entity.RentalStatus;
import edu.aitu.oop3.exception.CarNotAvailableException;
import edu.aitu.oop3.exception.EntityNotFoundException;
import edu.aitu.oop3.exception.InvalidDriverAgeException;
import edu.aitu.oop3.exception.RentalOverlapException;
import edu.aitu.oop3.repository.CarRepository;
import edu.aitu.oop3.repository.CustomerRepository;
import edu.aitu.oop3.repository.PaymentRepository;
import edu.aitu.oop3.repository.RentalRepository;
import edu.aitu.oop3.service.PricingService;
import edu.aitu.oop3.service.RentalService;
import edu.aitu.oop3.db.DatabaseConnection;

import java.sql.Connection;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class DefaultRentalService implements RentalService {

    private static final int MIN_DRIVER_AGE = 21;

    private final CarRepository carRepository;
    private final CustomerRepository customerRepository;
    private final RentalRepository rentalRepository;
    private final PaymentRepository paymentRepository;
    private final PricingService pricingService;

    public DefaultRentalService(
            CarRepository carRepository,
            CustomerRepository customerRepository,
            RentalRepository rentalRepository,
            PaymentRepository paymentRepository,
            PricingService pricingService
    ) {
        this.carRepository = carRepository;
        this.customerRepository = customerRepository;
        this.rentalRepository = rentalRepository;
        this.paymentRepository = paymentRepository;
        this.pricingService = pricingService;
    }

    @Override
    public Rental createRental(long customerId, long carId, LocalDate startDate, LocalDate endDate) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);
            try {
                var customer = customerRepository.findById(conn, customerId)
                        .orElseThrow(() -> new EntityNotFoundException("Customer not found: " + customerId));
                if (customer.getAge() < MIN_DRIVER_AGE) {
                    throw new InvalidDriverAgeException("Driver age is invalid: " + customer.getAge());
                }

                Car car = carRepository.findById(conn, carId)
                        .orElseThrow(() -> new EntityNotFoundException("Car not found: " + carId));
                if (!car.isAvailable()) {
                    throw new CarNotAvailableException("Car not available: " + carId);
                }

                if (rentalRepository.existsOverlap(conn, carId, startDate, endDate)) {
                    throw new RentalOverlapException("Rental overlap for car: " + carId);
                }

                Rental rental = new Rental();
                rental.setCarId(carId);
                rental.setCustomerId(customerId);
                rental.setStartDate(startDate);
                rental.setEndDate(endDate);
                rental.setStatus(RentalStatus.OPEN);

                long rentalId = rentalRepository.create(conn, rental);
                carRepository.setAvailability(conn, carId, false);

                conn.commit();

                rental.setId(rentalId);
                rental.setStatus(RentalStatus.OPEN);
                return rental;
            } catch (Exception e) {
                conn.rollback();
                throw e;
            } finally {
                conn.setAutoCommit(true);
            }
        } catch (RuntimeException re) {
            throw re;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public int closeRental(long rentalId, String paymentMethod) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);
            try {
                Rental rental = rentalRepository.findOpenById(conn, rentalId)
                        .orElseThrow(() -> new EntityNotFoundException("Open rental not found: " + rentalId));

                Car car = carRepository.findById(conn, rental.getCarId())
                        .orElseThrow(() -> new EntityNotFoundException("Car not found: " + rental.getCarId()));

                int total = pricingService.calculateTotalCost(car, rental.getStartDate(), rental.getEndDate());

                LocalDateTime now = LocalDateTime.now();
                rentalRepository.close(conn, rentalId, total, now);

                Payment payment = new Payment();
                payment.setRentalId(rentalId);
                payment.setAmount(total);
                payment.setPaidAt(now);
                payment.setMethod(paymentMethod == null ? "CARD" : paymentMethod);

                paymentRepository.create(conn, payment);

                carRepository.setAvailability(conn, rental.getCarId(), true);

                conn.commit();
                return total;
            } catch (Exception e) {
                conn.rollback();
                throw e;
            } finally {
                conn.setAutoCommit(true);
            }
        } catch (RuntimeException re) {
            throw re;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}