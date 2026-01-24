
package edu.aitu.oop3.entity;

import java.time.LocalDateTime;

public class Payment {
    private Long id;
    private Long rentalId;
    private int amount;
    private LocalDateTime paidAt;
    private String method;

    public Payment() {}

    public Payment(Long id, Long rentalId, int amount, LocalDateTime paidAt, String method) {
        this.id = id;
        this.rentalId = rentalId;
        this.amount = amount;
        this.paidAt = paidAt;
        this.method = method;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getRentalId() { return rentalId; }
    public void setRentalId(Long rentalId) { this.rentalId = rentalId; }

    public int getAmount() { return amount; }
    public void setAmount(int amount) { this.amount = amount; }

    public LocalDateTime getPaidAt() { return paidAt; }
    public void setPaidAt(LocalDateTime paidAt) { this.paidAt = paidAt; }

    public String getMethod() { return method; }
    public void setMethod(String method) { this.method = method; }
}