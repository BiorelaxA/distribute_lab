package com.example;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Map;
import java.util.Scanner;

public class AuctionClient {
    public static void main(String[] args) {
        try {
            // 1. 连接到 RMI 注册表
            Registry registry = LocateRegistry.getRegistry("localhost", 1099);
            Auction auction = (Auction) registry.lookup("AuctionService");

            Scanner scanner = new Scanner(System.in);

            // 2. 获取并展示所有拍卖物品及当前价格
            System.out.println("=== 当前拍卖物品列表 ===");
            Map<String, Double> items = auction.getAllItems();
            items.forEach((id, price) -> System.out.printf("Item ID: %s, Current Price: %.2f%n", id, price));
            System.out.println("========================");

            // 3. 让用户输入要竞拍的物品 ID
            System.out.print("Enter item ID to bid on: ");
            String itemID = scanner.nextLine();

            // 4. 竞拍循环
            while (true) {
                double current = auction.getCurrentBid(itemID);
                System.out.printf("Current bid for %s: %.2f%n", itemID, current);

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
