package com.example;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Map;

/**
 * 拍卖远程接口，新增方法以便用户添加新物品
 */
public interface Auction extends Remote {
    boolean placeBid(String itemID, double bidAmount, String bidderName) throws RemoteException;

    double getCurrentBid(String itemID) throws RemoteException;

    Map<String, Double> getAllItems() throws RemoteException; // 获取所有物品

    boolean addItem(String itemID, double startingPrice) throws RemoteException; // 添加新物品
}
