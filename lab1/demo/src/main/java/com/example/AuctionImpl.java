package com.example;

import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;

/**
 * AuctionImpl 实现了拍卖逻辑，并维护 itemID->当前最高出价 的映射
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
        // 返回一个拷贝，防止客户端修改
        return new HashMap<>(currentBids);
    }
}
