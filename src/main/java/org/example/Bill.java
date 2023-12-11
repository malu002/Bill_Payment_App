package org.example;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Bill {

    private static int billIdCounter = 1;
    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private int billId;
    private String type;
    private double amount;
    private LocalDate dueDate;
    private String state;
    private String provider;

    public Bill(String type, double amount, LocalDate dueDate, String state, String provider) {
        this.billId = billIdCounter++;
        this.type = type;
        this.amount = amount;
        this.dueDate = dueDate;
        this.state = state;
        this.provider = provider;
    }

    public String toString() {
        return String.format("%d. %s %.2f %s %s %s", billId, type, amount, dueDate, state, provider);
    }

    public int getBillId() {
        return billId;
    }

    public String getType() {
        return type;
    }

    public double getAmount() {
        return amount;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public String getState() {
        return state;
    }

    public String getProvider() {
        return provider;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }
}
