package edu.aitu.oop3.entity;

public class Customer {
    private Long id;
    private String fullName;
    private int age;
    private String driverLicenseNumber;

    public Customer() {}

    public Customer(Long id, String fullName, int age, String driverLicenseNumber) {
        this.id = id;
        this.fullName = fullName;
        this.age = age;
        this.driverLicenseNumber = driverLicenseNumber;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }

    public String getDriverLicenseNumber() { return driverLicenseNumber; }
    public void setDriverLicenseNumber(String driverLicenseNumber) { this.driverLicenseNumber = driverLicenseNumber; }
}
