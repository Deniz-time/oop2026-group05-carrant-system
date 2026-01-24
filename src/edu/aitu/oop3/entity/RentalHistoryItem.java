package edu.aitu.oop3.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class RentalHistoryItem {
    private long rentalId;

    private long carId;
    private String carBrand;
    private String carModel;

    private long customerId;
    private String customerName;

    private LocalDate startDate;
    private LocalDate endDate;

    private String status;
    private Integer totalCost;

    private LocalDateTime createdAt;
    private LocalDateTime closedAt;

    public long getRentalId() { return rentalId; }
    public void setRentalId(long rentalId) { this.rentalId = rentalId; }

    public long getCarId() { return carId; }
    public void setCarId(long carId) { this.carId = carId; }

    public String getCarBrand() { return carBrand; }
    public void setCarBrand(String carBrand) { this.carBrand = carBrand; }

    public String getCarModel() { return carModel; }
    public void setCarModel(String carModel) { this.carModel = carModel; }

    public long getCustomerId() { return customerId; }
    public void setCustomerId(long customerId) { this.customerId = customerId; }

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }

    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Integer getTotalCost() { return totalCost; }
    public void setTotalCost(Integer totalCost) { this.totalCost = totalCost; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getClosedAt() { return closedAt; }
    public void setClosedAt(LocalDateTime closedAt) { this.closedAt = closedAt; }
}
