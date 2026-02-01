package edu.aitu.oop3.app;

import edu.aitu.oop3.repository.CarRepository;
import edu.aitu.oop3.repository.CustomerRepository;
import edu.aitu.oop3.repository.PaymentRepository;
import edu.aitu.oop3.repository.RentalRepository;
import edu.aitu.oop3.repository.jdbc.JdbcCarRepository;
import edu.aitu.oop3.repository.jdbc.JdbcCustomerRepository;
import edu.aitu.oop3.repository.jdbc.JdbcPaymentRepository;
import edu.aitu.oop3.repository.jdbc.JdbcRentalRepository;
import edu.aitu.oop3.service.CarInventoryService;
import edu.aitu.oop3.service.PricingService;
import edu.aitu.oop3.service.RentalService;
import edu.aitu.oop3.service.impl.DefaultCarInventoryService;
import edu.aitu.oop3.service.impl.DefaultPricingService;
import edu.aitu.oop3.service.impl.DefaultRentalService;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        CarRepository carRepo = new JdbcCarRepository();
        CustomerRepository customerRepo = new JdbcCustomerRepository();
        RentalRepository rentalRepo = new JdbcRentalRepository();
        PaymentRepository paymentRepo = new JdbcPaymentRepository();

        PricingService pricingService = new DefaultPricingService();
        CarInventoryService inventoryService = new DefaultCarInventoryService(carRepo);
        RentalService rentalService = new DefaultRentalService(
                carRepo, customerRepo, rentalRepo, paymentRepo, pricingService
        );

        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\n1) Search available cars");
            System.out.println("2) Create rental");
            System.out.println("3) Close rental (total cost)");
            System.out.println("4) Rental history");
            System.out.println("5) Rental history by customer");
            System.out.println("X) Add car");
            System.out.println("0) Exit");
            System.out.print("> ");
            String choice = sc.nextLine().trim();

            try {
                if ("0".equalsIgnoreCase(choice)) break;

                if ("1".equals(choice)) {
                    System.out.print("from (yyyy-mm-dd): ");
                    LocalDate from = LocalDate.parse(sc.nextLine().trim());
                    System.out.print("to (yyyy-mm-dd): ");
                    LocalDate to = LocalDate.parse(sc.nextLine().trim());

                    var cars = inventoryService.searchAvailableCars(from, to);
                    cars.sort((a, b) -> Integer.compare(a.getDailyPrice(), b.getDailyPrice()));

                    for (var c : cars) {
                        System.out.println(c.getId() + " | " + c.getBrand() + " " + c.getModel() + " | " + c.getDailyPrice());
                    }

                } else if ("2".equals(choice)) {
                    System.out.print("customerId: ");
                    long customerId = Long.parseLong(sc.nextLine().trim());
                    System.out.print("carId: ");
                    long carId = Long.parseLong(sc.nextLine().trim());
                    System.out.print("start (yyyy-mm-dd): ");
                    LocalDate start = LocalDate.parse(sc.nextLine().trim());
                    System.out.print("end (yyyy-mm-dd): ");
                    LocalDate end = LocalDate.parse(sc.nextLine().trim());

                    var rental = rentalService.createRental(customerId, carId, start, end);
                    System.out.println("Created rental id: " + rental.getId());

                } else if ("3".equals(choice)) {
                System.out.print("rentalId: ");
                long rentalId = Long.parseLong(sc.nextLine().trim());
                System.out.print("payment method (CARD/CASH/etc): ");
                String method = sc.nextLine().trim();

                int total = rentalService.closeRental(rentalId, method);
                System.out.println("Total cost: " + total);

                try (var conn = edu.aitu.oop3.db.DatabaseConnection.getConnection()) {
                    var rentalOpt = rentalRepo.findById(conn, rentalId);
                    if (rentalOpt.isEmpty()) {
                        System.out.println("ERROR: Rental not found: " + rentalId);
                        continue;
                    }
                    var rental = rentalOpt.get();

                    var carOpt = carRepo.findById(conn, rental.getCarId());
                    if (carOpt.isEmpty()) {
                        System.out.println("ERROR: Car not found: " + rental.getCarId());
                        continue;
                    }
                    var car = carOpt.get();

                    long daysL = java.time.temporal.ChronoUnit.DAYS.between(rental.getStartDate(), rental.getEndDate()) + 1;
                    int days = Math.toIntExact(daysL);

                    var cfg = edu.aitu.oop3.config.FleetConfig.getInstance();
                    int insuranceCost = cfg.isInsuranceEnabled() ? cfg.getInsuranceDailyFee() * days : 0;

                    var contract = edu.aitu.oop3.entity.RentalContract.builder()
                            .rentalId(rentalId)
                            .customerId(rental.getCustomerId())
                            .carId(rental.getCarId())
                            .carType(car.getType())
                            .carName(car.getBrand() + " " + car.getModel())
                            .startDate(rental.getStartDate())
                            .endDate(rental.getEndDate())
                            .dailyPrice(car.getDailyPrice())
                            .days(days)
                            .insuranceCost(insuranceCost)
                            .totalCost(total + insuranceCost)
                            .build();

                    System.out.println(contract.toPrettyString());
                }
                } else if ("4".equals(choice)) {
                    try (var conn = edu.aitu.oop3.db.DatabaseConnection.getConnection()) {
                        var list = rentalRepo.findHistoryAll(conn);
                        for (var h : list) {
                            System.out.println(
                                    h.getRentalId() + " | " +
                                            h.getCustomerId() + " " + h.getCustomerName() + " | " +
                                            h.getCarId() + " " + h.getCarBrand() + " " + h.getCarModel() + " | " +
                                            h.getStartDate() + " -> " + h.getEndDate() + " | " +
                                            h.getStatus() + " | total=" + (h.getTotalCost() == null ? "-" : h.getTotalCost())
                            );
                        }
                    }

                } else if ("5".equals(choice)) {
                    System.out.print("customerId: ");
                    long cid = Long.parseLong(sc.nextLine().trim());
                    try (var conn = edu.aitu.oop3.db.DatabaseConnection.getConnection()) {
                        var list = rentalRepo.findHistoryByCustomerId(conn, cid);
                        for (var h : list) {
                            System.out.println(
                                    h.getRentalId() + " | " +
                                            h.getCarId() + " " + h.getCarBrand() + " " + h.getCarModel() + " | " +
                                            h.getStartDate() + " -> " + h.getEndDate() + " | " +
                                            h.getStatus() + " | total=" + (h.getTotalCost() == null ? "-" : h.getTotalCost())
                            );
                        }
                    }

                } else if ("X".equalsIgnoreCase(choice)) {
                    System.out.print("type (ECONOMY/SUV/ELECTRIC): ");
                    String t = sc.nextLine().trim();
                    var type = edu.aitu.oop3.entity.CarType.fromDb(t);

                    System.out.print("brand: ");
                    String brand = sc.nextLine().trim();
                    System.out.print("model: ");
                    String model = sc.nextLine().trim();
                    System.out.print("daily price: ");
                    int price = Integer.parseInt(sc.nextLine().trim());

                    var car = edu.aitu.oop3.factory.CarFactory.createNew(type, brand, model, price);

                    try (var conn = edu.aitu.oop3.db.DatabaseConnection.getConnection()) {
                        long id = carRepo.create(conn, car);
                        System.out.println("Created car id: " + id);
                    }

                } else {
                    System.out.println("Unknown option");
                }

            } catch (Exception e) {
                System.out.println("ERROR: " + e.getMessage());
            }
        }
    }
}