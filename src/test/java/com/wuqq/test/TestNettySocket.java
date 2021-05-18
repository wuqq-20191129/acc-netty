package com.wuqq.test;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Arrays;

/**
 * @Classname TestNettySocket
 * @Description TODO
 * @Date 2021/1/20 9:21
 * @Created by mh
 */
public class TestNettySocket {
    public static void main(String[] args) {


         final byte STX_B = (byte) 0xEB;
         final byte ETX = 0x03;
         final byte QRY = 0x01;
         final byte DTA = 0x03;
         final byte[] HEADER = { STX_B, DTA, 0 };
         final byte[] QUERY = { STX_B, QRY, 0, 0, 0, ETX };
         int fromServer=0;
        int serial=0;
        try {
            Socket socket = new Socket("172.20.18.9",5001);

            //BufferedInputStream in = new BufferedInputStream(socket.getInputStream());

            while(true){

                try {
                    BufferedInputStream in = new BufferedInputStream(socket.getInputStream());
                    fromServer = in.read();
                    System.out.println("fromServer==="+fromServer);
                    if((byte)fromServer!=STX_B){
                        throw  new Exception();
                    }
                    fromServer = in.read();
                    System.out.println("fromServer==="+fromServer);
                    if((byte)fromServer!=QRY){
                        throw  new Exception();
                    }
                    fromServer = in.read();
                    System.out.println("fromServer==="+fromServer);


                    //if(serial<255){
                        QUERY[2]=(byte)fromServer;
                        OutputStream out = socket.getOutputStream();
                        System.out.println(Arrays.toString(QUERY));
                        out.write(QUERY);
                        out.flush();
                    //    serial++;
                    //}

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
