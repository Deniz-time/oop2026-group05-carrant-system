package edu.aitu.oop3.entity;

public enum CarType {
    ECONOMY, SUV, ELECTRIC;

    public static CarType fromDb(String s) {
        if (s == null) return ECONOMY;
        try { return CarType.valueOf(s.trim().toUpperCase()); }
        catch (Exception e) { return ECONOMY; }
    }

    public String toDb() {
        return name();
    }
}
