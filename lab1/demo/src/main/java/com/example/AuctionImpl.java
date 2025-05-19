package com.example;

import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;

/**
 * Auction 接口实现：维护每个 itemID 的最高出价和最高出价者
 */
public class AuctionImpl extends UnicastRemoteObject implements Auction {

    private final Map<String, Double> currentBids = new HashMap<>();
    private final Map<String, String> highestBidder = new HashMap<>();

    protected AuctionImpl() throws RemoteException {
        super();
        // 可初始化若干拍卖物品
        currentBids.put("item001", 0.0);
        highestBidder.put("item001", "No bids yet");
    }

    @Override
    public synchronized boolean placeBid(String itemID, double bid, String bidderName) throws RemoteException {
        double current = currentBids.getOrDefault(itemID, 0.0);
        if (bid > current) {
            currentBids.put(itemID, bid);
            highestBidder.put(itemID, bidderName);
            System.out.printf("New highest bid: %s bids %.2f on %s%n", bidderName, bid, itemID);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public synchronized String getCurrentBid(String itemID) throws RemoteException {
        double bid = currentBids.getOrDefault(itemID, 0.0);
        String name = highestBidder.getOrDefault(itemID, "No bids yet");
        return String.format("%.2f by %s", bid, name);
    }
}
