package com.example;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Map;

/**
 * 远程接口：定义出价和查询当前最高出价的方法
 */
public interface Auction extends Remote {
    boolean placeBid(String itemID, double bidAmount, String bidderName) throws RemoteException;

    double getCurrentBid(String itemID) throws RemoteException;

    Map<String, Double> getAllItems() throws RemoteException; // 新增
}