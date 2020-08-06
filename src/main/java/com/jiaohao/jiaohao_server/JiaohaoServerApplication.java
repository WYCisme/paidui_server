package com.jiaohao.jiaohao_server;

import lombok.SneakyThrows;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.context.ApplicationListener;

import java.net.*;
import java.util.Enumeration;


@SpringBootApplication
public class JiaohaoServerApplication implements ApplicationListener<WebServerInitializedEvent> {

    public static void main(String[] args) {
        SpringApplication.run(JiaohaoServerApplication.class, args);
    }

    @SneakyThrows
    @Override
    public void onApplicationEvent(WebServerInitializedEvent event) {
        try {
            Enumeration<NetworkInterface> faces = NetworkInterface.getNetworkInterfaces();
            while (faces.hasMoreElements()) { // 遍历网络接口
                NetworkInterface face = faces.nextElement();
                if (face.isLoopback() || face.isVirtual() || !face.isUp()) {
                    continue;
                }
                System.out.print("网络接口名：" + face.getDisplayName() + "，地址：");
                Enumeration<InetAddress> address = face.getInetAddresses();
                while (address.hasMoreElements()) { // 遍历网络地址
                    InetAddress addr = address.nextElement();
                    if (!addr.isLoopbackAddress() && addr.isSiteLocalAddress() && !addr.isAnyLocalAddress()) {
                        System.out.print(addr.getHostAddress() + " ");
                    }
                }
                System.out.println("");
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

}
