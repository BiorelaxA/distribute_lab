package com.example;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * 远程接口：定义出价和查询当前最高出价的方法
 */
public interface Auction extends Remote {
    /**
     * 客户端提交出价，如果成功更新最高出价返回 true，否则 false
     */
    boolean placeBid(String itemID, double bid, String bidderName) throws RemoteException;

    /**
     * 获取指定物品当前最高出价及出价者信息，格式如 "100.0 by Alice"
     */
    String getCurrentBid(String itemID) throws RemoteException;
}
