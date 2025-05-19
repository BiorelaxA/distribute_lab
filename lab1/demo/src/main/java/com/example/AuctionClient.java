package com.example;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

public class AuctionClient {
    public static void main(String[] args) {
        try {
            // 1. 连接到 RMI 注册表
            Registry registry = LocateRegistry.getRegistry("localhost", 1099);

            // 2. 查找远程对象存根
            Auction auction = (Auction) registry.lookup("AuctionService");

            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter item ID to bid on: ");
            String itemID = scanner.nextLine();

            while (true) {
                System.out.println("Current bid: " + auction.getCurrentBid(itemID));
                System.out.print("Your name (or q to quit): ");
                String name = scanner.nextLine();
                if ("q".equalsIgnoreCase(name))
                    break;

                System.out.print("Enter your bid: ");
                double bid = Double.parseDouble(scanner.nextLine());

                boolean success = auction.placeBid(itemID, bid, name);
                if (success) {
                    System.out.println("Bid successful!");
                } else {
                    System.out.println("Bid too low. Try again.");
                }
            }
            scanner.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
