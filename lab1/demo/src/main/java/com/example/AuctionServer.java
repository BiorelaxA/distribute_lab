package com.example;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class AuctionServer {
    public static void main(String[] args) {
        try {
            // 1. 启动 RMI 注册表并返回 Registry 实例，监听端口 1099
            Registry registry = LocateRegistry.createRegistry(1099);

            // 2. 创建远程对象实例
            AuctionImpl auction = new AuctionImpl();

            // 3. 将远程对象绑定到注册表中，名称为 "AuctionService"
            registry.rebind("AuctionService", auction);

            System.out.println("Auction RMI Server is running on port 1099...");

            // 4. 阻塞主线程，防止 JVM 退出
            Thread.currentThread().join();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
