package org.example;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        System.out.println("PaymentServiceApp");
        System.out.println("KEY:");
        System.out.println("CASH_IN");
        System.out.println("CREATE_BILL");
        System.out.println("UPDATE_BILL");
        System.out.println("DELETE_BILL");
        System.out.println("LIST_BILL");
        System.out.println("PAY");
        System.out.println("PAY_MULTIPLE");
        System.out.println("DUE_DATES");
        System.out.println("LIST_PAYMENT");
        System.out.println("SEARCH_BILL_BY_PROVIDER");
        System.out.println("LIST_PAYMENT_HISTORY");
        System.out.println("SCHEDULE");
        System.out.println("EXIT");
        Customer customer = new Customer();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.print("$ ");
            String command = scanner.nextLine().trim();

            String[] parts = command.split(" ");
            String action = parts[0].toUpperCase();

            switch (action) {
                case "CASH_IN":
                    double amount = Double.parseDouble(parts[1]);
                    customer.addFunds(amount);
                    break;

                case "CREATE_BILL":
                    String billType = parts[1];
                    double billAmount = Double.parseDouble(parts[2]);
                    LocalDate billDueDate = LocalDate.parse(parts[3], Bill.DATE_FORMATTER);
                    String billProvider = parts[4];
                    customer.createBill(billType, billAmount, billDueDate, billProvider);
                    break;

                case "UPDATE_BILL":
                    int updateBillId = Integer.parseInt(parts[1]);
                    String updateBillType = parts[2];
                    double updateBillAmount = Double.parseDouble(parts[3]);
                    LocalDate updateBillDueDate = LocalDate.parse(parts[4], Bill.DATE_FORMATTER);
                    String updateBillProvider = parts[5];
                    customer.updateBill(updateBillId, updateBillType, updateBillAmount, updateBillDueDate, updateBillProvider);
                    break;

                case "DELETE_BILL":
                    int deleteBillId = Integer.parseInt(parts[1]);
                    customer.deleteBill(deleteBillId);
                    break;

                case "LIST_BILL":
                    customer.listBills();
                    break;

                case "PAY":
                    int billId = Integer.parseInt(parts[1]);
                    customer.payBill(billId);
                    break;

                case "PAY_MULTIPLE":
                    List<Integer> billIds = Arrays.stream(parts).skip(1)
                            .map(Integer::parseInt)
                            .collect(Collectors.toList());
                    customer.payMultipleBills(billIds);
                    break;

                case "DUE_DATES":
                    customer.displayUpcomingDueDates();
                    break;

                case "LIST_PAYMENT":
                    customer.listPayments();
                    break;

                case "SEARCH_BILL_BY_PROVIDER":
                    String provider = parts[1];
                    customer.searchBillsByProvider(provider);
                    break;

                case "LIST_PAYMENT_HISTORY":
                    customer.displayPaymentHistory();
                    break;

                case "SCHEDULE":
                    int scheduledBillId = Integer.parseInt(parts[1]);
                    LocalDate scheduledDate = LocalDate.parse(parts[2], Bill.DATE_FORMATTER);
                    customer.scheduleBillPayment(scheduledBillId, scheduledDate);
                    break;

                case "EXIT":
                    System.out.println("Good bye!");
                    scanner.close();
                    System.exit(0);
                    break;

                default:
                    System.out.println("Invalid command");
            }
        }
    }
}