package com.example;

import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;

/**
 * 拍卖实现类，支持添加新物品和竞价
 */
public class AuctionImpl extends UnicastRemoteObject implements Auction {
    private final Map<String, Double> currentBids;

    protected AuctionImpl() throws RemoteException {
        super();
        currentBids = new HashMap<>();
        // 初始化一些示例物品
        currentBids.put("item001", 100.0);
        currentBids.put("item002", 250.0);
        currentBids.put("item003", 75.0);
    }

    @Override
    public synchronized boolean placeBid(String itemID, double bidAmount, String bidderName) throws RemoteException {
        double current = currentBids.getOrDefault(itemID, 0.0);
        if (bidAmount > current) {
            currentBids.put(itemID, bidAmount);
            System.out.printf("New bid: %s bids %.2f on %s%n", bidderName, bidAmount, itemID);
            return true;
        }
        return false;
    }

    @Override
    public synchronized double getCurrentBid(String itemID) throws RemoteException {
        return currentBids.getOrDefault(itemID, 0.0);
    }

    @Override
    public synchronized Map<String, Double> getAllItems() throws RemoteException {
        return new HashMap<>(currentBids); // 返回物品和当前竞价
    }

    @Override
    public synchronized boolean addItem(String itemID, double startingPrice) throws RemoteException {
        if (!currentBids.containsKey(itemID)) {
            currentBids.put(itemID, startingPrice); // 添加新的物品和价格
            System.out.printf("Added new item: %s with starting price: %.2f%n", itemID, startingPrice);
            return true;
        }
        return false; // 如果物品ID已存在，则不能添加
    }
}
