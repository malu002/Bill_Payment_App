package org.example;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class Customer {

    private double availableFunds;
    private List<Bill> bills;
    private List<Bill> scheduledPayments;

    private List<Bill> paymentHistory = new ArrayList<>();

    public Customer() {
        this.availableFunds = 0;
        this.bills = new ArrayList<>();
        this.scheduledPayments = new ArrayList<>();
    }

    public void addFunds(double amount) {
        this.availableFunds += amount;
        System.out.println("Your available balance: " + this.availableFunds);
    }

    public void createBill(String type, double amount, LocalDate dueDate, String provider) {
        Bill bill = new Bill(type, amount, dueDate, "NOT_PAID", provider);
        bills.add(bill);
    }

    public void updateBill(int billId, String type, double amount, LocalDate dueDate, String provider) {
        Bill bill = findBillById(billId);
        if (bill != null) {
            bill.setType(type);
            bill.setAmount(amount);
            bill.setDueDate(dueDate);
            bill.setProvider(provider);
            System.out.println("Bill updated successfully:");
            System.out.println(bill);
        } else {
            System.out.println("Sorry! Not found a bill with such id");
        }
    }

    public void deleteBill(int billId) {
        Iterator<Bill> iterator = bills.iterator();
        while (iterator.hasNext()) {
            Bill bill = iterator.next();
            if (bill.getBillId() == billId) {
                iterator.remove();
                System.out.println("Bill deleted successfully.");
                System.out.println(bill);
                return;
            }
        }
        System.out.println("Sorry! Not found a bill with such id");
    }

    public void listBills() {
        System.out.println("Bill No. Type Amount Due Date State PROVIDER");
        for (Bill bill : bills) {
            System.out.println(bill);
        }
    }

    public void payBill(int billId) {
        Bill bill = findBillById(billId);
        if (bill != null && "NOT_PAID".equals(bill.getState()) && availableFunds >= bill.getAmount()) {
            availableFunds -= bill.getAmount();
            bill.setState("PROCESSED");
            paymentHistory.add(bill);
            System.out.println("Payment has been completed for Bill with id " + billId + ".");
            System.out.println("Your current balance is: " + this.availableFunds);
        } else if (bill == null) {
            System.out.println("Sorry! Not found a bill with such id");
        } else if (!"NOT_PAID".equals(bill.getState())) {
            System.out.println("Sorry! This bill has already been paid.");
        } else {
            System.out.println("Sorry! Not enough funds to proceed with payment.");
        }
    }

    public void payMultipleBills(List<Integer> billIds) {
        double totalAmount = 0;

        // Sort bills based on due dates
        List<Bill> sortedBills = new ArrayList<>();
        for (int billId : billIds) {
            Bill bill = findBillById(billId);
            if (bill != null && "NOT_PAID".equals(bill.getState())) {
                sortedBills.add(bill);
            } else {
                System.out.println("Skipping payment for bill with id " + billId + ". Either it is already paid or not found.");
            }
        }

        sortedBills.sort(Comparator.comparing(Bill::getDueDate));

        for (Bill bill : sortedBills) {
            if (availableFunds >= bill.getAmount()) {
                availableFunds -= bill.getAmount();
                bill.setState("PROCESSED");
                paymentHistory.add(bill);
                System.out.println("Payment has been completed for Bill with id " + bill.getBillId());
                totalAmount += bill.getAmount();
            } else {
                System.out.println("Not enough funds to pay for Bill with id " + bill.getBillId());
            }
        }

        System.out.println("Total amount paid: " + totalAmount);
        System.out.println("Your current balance is: " + this.availableFunds);
    }

    public void displayUpcomingDueDates() {
        System.out.println("Upcoming Due Dates:");
        for (Bill bill : bills) {
            if ("NOT_PAID".equals(bill.getState())) {
                long daysUntilDue = ChronoUnit.DAYS.between(LocalDate.now(), bill.getDueDate());
                System.out.println("Bill ID " + bill.getBillId() + ": " + daysUntilDue + " days until due.");
            }
        }
    }

    public void listPayments() {
        System.out.println("No. Amount Payment Date State Bill Id");
        int paymentId = 1;
        for (Bill bill : bills) {
            if ("PROCESSED".equals(bill.getState())) {
                System.out.println(paymentId++ + ". " + bill.getAmount() + " " +
                        bill.getDueDate() + " PROCESSED " + bill.getBillId());
            }
        }
        for (Bill scheduledPayment : scheduledPayments) {
            System.out.println(paymentId++ + ". " + scheduledPayment.getAmount() + " " +
                    scheduledPayment.getDueDate() + " PENDING " + scheduledPayment.getBillId());
        }
    }

    public void scheduleBillPayment(int billId, LocalDate scheduledDate) {
        Bill bill = findBillById(billId);
        if (bill != null && "NOT_PAID".equals(bill.getState())) {
            Bill scheduledPayment = new Bill(bill.getType(), bill.getAmount(), scheduledDate, "PENDING", bill.getProvider());
            scheduledPayments.add(scheduledPayment);
            System.out.println("Payment for bill id " + billId + " is scheduled on " + scheduledDate);
        } else if (bill == null) {
            System.out.println("Sorry! Not found a bill with such id");
        } else {
            System.out.println("Sorry! This bill has already been paid.");
        }
    }

    public void displayPaymentHistory() {
        System.out.println("Payment Transaction History:");
        for (Bill bill : paymentHistory) {
            System.out.println("Paid Bill ID " + bill.getBillId() + " on " + LocalDate.now());
        }
        for (Bill scheduledPayment : scheduledPayments) {
            System.out.println("Scheduled Payment for Bill ID " + scheduledPayment.getBillId() +
                    " on " + scheduledPayment.getDueDate());
        }
    }

    public void searchBillsByProvider(String provider) {
        System.out.println("Bill No. Type Amount Due Date State PROVIDER");
        for (Bill bill : bills) {
            if (provider.equals(bill.getProvider())) {
                System.out.println(bill);
            }
        }
    }

    private Bill findBillById(int billId) {
        for (Bill bill : bills) {
            if (bill.getBillId() == billId) {
                return bill;
            }
        }
        return null;
    }

    public double getAvailableFunds() {
        return availableFunds;
    }

    public List<Bill> getBills() {
        return this.bills;
    }

    public List<Bill> getScheduledPayments() {
        return this.scheduledPayments;
    }
}
