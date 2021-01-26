package com.wuqq.test;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * @Classname TestNettySocket
 * @Description TODO
 * @Date 2021/1/20 9:21
 * @Created by mh
 */
public class TestNettySocket {
    public static void main(String[] args) {
        try {
            Socket socket = new Socket("172.20.18.34",5001);

            BufferedInputStream in = new BufferedInputStream(socket.getInputStream());

            while(true){
                int str  = in.read();
                System.out.println("recived :"+str);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
