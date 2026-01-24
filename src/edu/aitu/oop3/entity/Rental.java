
package edu.aitu.oop3.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Rental {
    private Long id;
    private Long carId;
    private Long customerId;
    private LocalDate startDate;
    private LocalDate endDate;
    private RentalStatus status;
    private Integer totalCost;
    private LocalDateTime createdAt;
    private LocalDateTime closedAt;

    public Rental() {}

    public Rental(Long id, Long carId, Long customerId, LocalDate startDate, LocalDate endDate,
                  RentalStatus status, Integer totalCost, LocalDateTime createdAt, LocalDateTime closedAt) {
        this.id = id;
        this.carId = carId;
        this.customerId = customerId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
        this.totalCost = totalCost;
        this.createdAt = createdAt;
        this.closedAt = closedAt;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getCarId() { return carId; }
    public void setCarId(Long carId) { this.carId = carId; }

    public Long getCustomerId() { return customerId; }
    public void setCustomerId(Long customerId) { this.customerId = customerId; }

    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }

    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }

    public RentalStatus getStatus() { return status; }
    public void setStatus(RentalStatus status) { this.status = status; }

    public Integer getTotalCost() { return totalCost; }
    public void setTotalCost(Integer totalCost) { this.totalCost = totalCost; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getClosedAt() { return closedAt; }
    public void setClosedAt(LocalDateTime closedAt) { this.closedAt = closedAt; }
}
