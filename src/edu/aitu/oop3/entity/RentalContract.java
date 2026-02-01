package edu.aitu.oop3.entity;

import java.time.LocalDate;

public class RentalContract {
    private final long rentalId;
    private final long customerId;
    private final long carId;
    private final CarType carType;
    private final String carName;
    private final LocalDate startDate;
    private final LocalDate endDate;
    private final int dailyPrice;
    private final int days;
    private final int insuranceCost;
    private final int totalCost;

    private RentalContract(Builder b) {
        this.rentalId = b.rentalId;
        this.customerId = b.customerId;
        this.carId = b.carId;
        this.carType = b.carType;
        this.carName = b.carName;
        this.startDate = b.startDate;
        this.endDate = b.endDate;
        this.dailyPrice = b.dailyPrice;
        this.days = b.days;
        this.insuranceCost = b.insuranceCost;
        this.totalCost = b.totalCost;
    }

    public static Builder builder() { return new Builder(); }

    public String toPrettyString() {
        return "CONTRACT #" + rentalId + "\n"
                + "CustomerId: " + customerId + "\n"
                + "Car: " + carId + " " + carName + " (" + carType + ")\n"
                + "Dates: " + startDate + " -> " + endDate + "\n"
                + "Price/day: " + dailyPrice + "\n"
                + "Days: " + days + "\n"
                + "Insurance: " + insuranceCost + "\n"
                + "TOTAL: " + totalCost + "\n";
    }

    public static final class Builder {
        private long rentalId;
        private long customerId;
        private long carId;
        private CarType carType = CarType.ECONOMY;
        private String carName = "";
        private LocalDate startDate;
        private LocalDate endDate;
        private int dailyPrice;
        private int days;
        private int insuranceCost;
        private int totalCost;

        public Builder rentalId(long v) { this.rentalId = v; return this; }
        public Builder customerId(long v) { this.customerId = v; return this; }
        public Builder carId(long v) { this.carId = v; return this; }
        public Builder carType(CarType v) { this.carType = v; return this; }
        public Builder carName(String v) { this.carName = v; return this; }
        public Builder startDate(LocalDate v) { this.startDate = v; return this; }
        public Builder endDate(LocalDate v) { this.endDate = v; return this; }
        public Builder dailyPrice(int v) { this.dailyPrice = v; return this; }
        public Builder days(int v) { this.days = v; return this; }
        public Builder insuranceCost(int v) { this.insuranceCost = v; return this; }
        public Builder totalCost(int v) { this.totalCost = v; return this; }

        public RentalContract build() { return new RentalContract(this); }
    }
}