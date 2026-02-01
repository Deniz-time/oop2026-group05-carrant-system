package edu.aitu.oop3.config;

public final class FleetConfig {
    private static final FleetConfig INSTANCE = new FleetConfig();

    private final int insuranceDailyFee;
    private final boolean insuranceEnabled;

    private FleetConfig() {
        this.insuranceDailyFee = 50;
        this.insuranceEnabled = true;
    }

    public static FleetConfig getInstance() {
        return INSTANCE;
    }

    public int getInsuranceDailyFee() {
        return insuranceDailyFee;
    }

    public boolean isInsuranceEnabled() {
        return insuranceEnabled;
    }
}
