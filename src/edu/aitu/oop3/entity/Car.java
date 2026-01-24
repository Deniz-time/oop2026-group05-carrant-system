package edu.aitu.oop3.entity;

public class Car {
    private Long id;
    private String brand;
    private String model;
    private int dailyPrice;
    private boolean available;

    public Car() {}

    public Car(Long id, String brand, String model, int dailyPrice, boolean available) {
        this.id = id;
        this.brand = brand;
        this.model = model;
        this.dailyPrice = dailyPrice;
        this.available = available;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getBrand() { return brand; }
    public void setBrand(String brand) { this.brand = brand; }

    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }

    public int getDailyPrice() { return dailyPrice; }
    public void setDailyPrice(int dailyPrice) { this.dailyPrice = dailyPrice; }

    public boolean isAvailable() { return available; }
    public void setAvailable(boolean available) { this.available = available; }
}
