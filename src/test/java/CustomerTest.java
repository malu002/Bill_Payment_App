import org.example.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public class CustomerTest {
    private Customer customer;

    @BeforeEach
    public void setUp() {
        customer = new Customer();
    }

    @Test
    public void testAddFunds() {
        customer.addFunds(1000000);
        assertEquals(1000000, customer.getAvailableFunds(), 0.001);
    }

    @Test
    public void testCreateBill() {
        customer.createBill("ELECTRIC", 200000, LocalDate.parse("2023-10-25"), "EVN HCMC");
        assertEquals(1, customer.getBills().size());
    }

    @Test
    public void testUpdateBill() {
        customer.createBill("ELECTRIC", 200000, LocalDate.parse("2023-10-25"), "EVN HCMC");
        customer.updateBill(1, "UPDATED_ELECTRIC", 250000, LocalDate.parse("2023-11-30"), "EVN HCMC");
        assertEquals("UPDATED_ELECTRIC", customer.getBills().get(0).getType());
        assertEquals(250000, customer.getBills().get(0).getAmount(), 0.001);
    }

    @Test
    public void testDeleteBill() {
        customer.createBill("ELECTRIC", 200000, LocalDate.parse("2023-10-25"), "EVN HCMC");
        customer.deleteBill(1);
        assertEquals(0, customer.getBills().size());
    }

    @Test
    public void testListBills() {
        customer.createBill("ELECTRIC", 200000, LocalDate.parse("2023-10-25"), "EVN HCMC");
        customer.createBill("WATER", 175000, LocalDate.parse("2023-10-30"), "SAVACO HCMC");
        assertEquals("Bill No. Type Amount Due Date State PROVIDER\r\n" +
                "1. ELECTRIC 200000.00 2023-10-25 NOT_PAID EVN HCMC\r\n" +
                "2. WATER 175000.00 2023-10-30 NOT_PAID SAVACO HCMC\r\n", TestUtils.captureSystemOut(() -> customer.listBills()));
    }

    @Test
    public void testPayBill() {
        customer.addFunds(500000);
        customer.createBill("ELECTRIC", 200000, LocalDate.parse("2023-10-25"), "EVN HCMC");
        customer.payBill(1);
        assertEquals(300000, customer.getAvailableFunds(), 0.001);
        assertEquals("PROCESSED", customer.getBills().get(0).getState());
    }

    @Test
    public void testPayMultipleBills() {
        customer.addFunds(1000000);
        customer.createBill("ELECTRIC", 200000, LocalDate.parse("2023-10-25"), "EVN HCMC");
        customer.createBill("WATER", 175000, LocalDate.parse("2023-10-30"), "SAVACO HCMC");
        customer.payMultipleBills(Arrays.asList(1, 2));
        assertEquals(625000, customer.getAvailableFunds(), 0.001);
        assertEquals("PROCESSED", customer.getBills().get(0).getState());
        assertEquals("PROCESSED", customer.getBills().get(1).getState());
    }

    @Test
    public void testScheduleBillPayment() {
        customer.addFunds(1000000);
        customer.createBill("ELECTRIC", 200000, LocalDate.parse("2023-10-25"), "EVN HCMC");
        customer.scheduleBillPayment(1, LocalDate.parse("2023-10-20"));
        assertEquals(1, customer.getScheduledPayments().size());
    }

    @Test
    public void testDisplayUpcomingDueDates() {
        customer.createBill("WATER", 175000, LocalDate.parse("2024-01-15"), "SAVACO HCMC");
        customer.createBill("INTERNET", 800000, LocalDate.parse("2024-01-30"), "VNPT");

        long daysUntilDueWater = ChronoUnit.DAYS.between(LocalDate.now(), LocalDate.parse("2024-01-15"));
        long daysUntilDueInternet = ChronoUnit.DAYS.between(LocalDate.now(), LocalDate.parse("2024-01-30"));

        assertEquals("Upcoming Due Dates:\r\nBill ID 1: " +daysUntilDueWater + " days until due.\r\nBill ID 2: " +daysUntilDueInternet + " days until due.", TestUtils.captureSystemOut(() -> customer.displayUpcomingDueDates()));
    }

    @Test
    public void testListPayments() {
        customer.addFunds(1000000);
        customer.createBill("ELECTRIC", 200000, LocalDate.parse("2023-10-25"), "EVN HCMC");
        customer.payBill(1);

        assertEquals("No. Amount Payment Date State Bill Id\r\n1. 200000.0 2023-10-25 PROCESSED 1", TestUtils.captureSystemOut(() -> customer.listPayments()));
    }

    @Test
    public void testDisplayPaymentHistory() {
        customer.addFunds(500000);
        customer.createBill("ELECTRIC", 200000, LocalDate.parse("2023-10-25"), "EVN HCMC");
        customer.payBill(1);

        assertEquals("Payment Transaction History:\r\nPaid Bill ID 1 on " + LocalDate.now(), TestUtils.captureSystemOut(() -> customer.displayPaymentHistory()));
    }
}
