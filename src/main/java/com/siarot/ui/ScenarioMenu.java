package com.siarot.ui;

import com.siarot.dao.ScenarioDao;
import com.siarot.model.ScenarioEntity;

import java.util.List;
import java.util.Scanner;

public class ScenarioMenu {
    private static final String LINE = "=".repeat(52);
    private final ScenarioDao dao;
    private final Scanner scanner = new Scanner(System.in);

    public ScenarioMenu(ScenarioDao dao) {
        this.dao = dao;
    }

    public void run() {
        boolean running = true;
        while (running) {
            printMenu();
            int choice = readInt("Enter choice: ");
            switch (choice) {
                case 1 -> addOrder();
                case 2 -> viewOrders();
                case 3 -> {
                    System.out.println("\nExiting system... Thank you!");
                    running = false;
                }
                default -> System.out.println("\nInvalid choice. Please enter 1, 2, or 3.\n");
            }
        }
        scanner.close();
    }

    private void printMenu() {
        System.out.println(LINE);
        System.out.println("              COFFEE SHOP ORDERS SYSTEM");
        System.out.println(LINE);
        System.out.println("[1] Add New Order");
        System.out.println("[2] View Active Orders");
        System.out.println("[3] Exit System");
        System.out.println(LINE);
    }

    private void addOrder() {
        System.out.println("\n--- ADD NEW ORDER ---");
        String name = readNonEmpty("Enter Customer Name: ");
        String drink = readNonEmpty("Enter Drink Item: ");
        double price = readPrice("Enter Price: ");
        String status = readStatus("Enter Status (PENDING / PREPARING / COMPLETED): ");

        if (dao.insert(new ScenarioEntity(0, name, drink, price, status))) {
            System.out.println("\nSUCCESS: Order added to queue!\n");
        } else {
            System.out.println("\nERROR: Failed to add order.\n");
        }
    }

    private void viewOrders() {
        List<ScenarioEntity> orders = dao.findAll();
        String border = "+----+--------------------+--------------------+------------+----------------+";
        String row = "| %-2d | %-18s | %-18s | %10.2f | %-14s |%n";
        String bar = "=".repeat(border.length());

        System.out.println("\n" + bar);
        System.out.println("                             ACTIVE COFFEE ORDERS");
        System.out.println(bar);
        System.out.println(border);
        System.out.printf("| %-2s | %-18s | %-18s | %-10s | %-14s |%n",
                "ID", "CUSTOMER NAME", "DRINK ITEM", "PRICE", "STATUS");
        System.out.println(border);

        double total = 0;
        for (ScenarioEntity o : orders) {
            System.out.printf(row, o.id(), o.customerName(), o.drinkItem(), o.price(), o.status());
            total += o.price();
        }
        System.out.println(border);
        System.out.println("Total Records Displayed: " + orders.size());
        System.out.printf("Grand Total: %.2f%n%n", total);
    }

    // ---------- Input helpers ----------
    // Everything is read with nextLine() so no leftover newline stays in the buffer.

    private String readNonEmpty(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            if (!input.isEmpty()) return input;
            System.out.println("Input cannot be empty. Try again.");
        }
    }

    private int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }

    private double readPrice(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                double price = Double.parseDouble(scanner.nextLine().trim());
                if (price > 0) return price;
                System.out.println("Price must be greater than 0.");
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid price (e.g., 165.00).");
            }
        }
    }

    private String readStatus(String prompt) {
        while (true) {
            System.out.print(prompt);
            String status = scanner.nextLine().trim().toUpperCase();
            if (status.equals("PENDING") || status.equals("PREPARING") || status.equals("COMPLETED")) {
                return status;
            }
            System.out.println("Invalid status. Use PENDING, PREPARING, or COMPLETED.");
        }
    }
}
