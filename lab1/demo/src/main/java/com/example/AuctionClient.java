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

            // 客户端交互
            while (true) {
                System.out.println("\n=== 拍卖系统 ===");
                System.out.println("1. 查看所有物品");
                System.out.println("2. 选择物品竞拍");
                System.out.println("3. 添加新物品");
                System.out.println("4. 退出");
                System.out.print("请选择操作: ");
                int choice = scanner.nextInt();
                scanner.nextLine(); // 清除缓冲区

                switch (choice) {
                    case 1:
                        // 查看所有物品
                        System.out.println("\n=== 当前拍卖物品列表 ===");
                        Map<String, Double> items = auction.getAllItems();
                        items.forEach(
                                (id, price) -> System.out.printf("Item ID: %s, Current Price: %.2f%n", id, price));
                        break;
                    case 2:
                        // 选择物品竞拍
                        System.out.print("Enter item ID to bid on: ");
                        String itemID = scanner.nextLine();

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
                        break;
                    case 3:
                        // 添加新物品
                        System.out.print("Enter new item ID: ");
                        String newItemID = scanner.nextLine();

                        System.out.print("Enter starting bid for the item: ");
                        double startingPrice = Double.parseDouble(scanner.nextLine());

                        boolean added = auction.addItem(newItemID, startingPrice);
                        if (added) {
                            System.out.println("Item added successfully.");
                        } else {
                            System.out.println("Item ID already exists.");
                        }
                        break;
                    case 4:
                        // 退出系统
                        System.out.println("Exiting auction system...");
                        scanner.close();
                        return;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                        break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
