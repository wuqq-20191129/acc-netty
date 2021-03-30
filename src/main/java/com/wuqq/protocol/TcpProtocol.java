package com.wuqq.protocol;

import java.util.Arrays;

/**
 * @Classname TcpProtocol
 * @Description 定义tcp/ip 协议
 * @Date 2021/3/30 9:17
 * @Created by mh
 */
public class TcpProtocol {

    private byte header = (byte)0xEB;

    private byte dataType;

    private  byte serialNo;

    private short len;

    private  byte[] data;

    private byte tail = 0x03;


    public  TcpProtocol(byte dataType,byte serialNo,short len,byte[] data){
            this.dataType = dataType;
            this.serialNo = serialNo;
            this.len =len;
            this.data =data;
    }

    public  TcpProtocol(){

    }

    @Override
    public String toString() {
        return "TcpProtocol==={"+
                   "header="+header+
                    "dataType="+dataType+
                    "serialNo="+serialNo+
                    "len="+len+
                    "data="+ Arrays.toString(data)+
                    "tail="+tail+
                    "}";
    }

    public byte getHeader() {
        return header;
    }

    public void setHeader(byte header) {
        this.header = header;
    }

    public byte getDataType() {
        return dataType;
    }

    public void setDataType(byte dataType) {
        this.dataType = dataType;
    }

    public byte getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(byte serialNo) {
        this.serialNo = serialNo;
    }

    public short getLen() {
        return len;
    }

    public void setLen(short len) {
        this.len = len;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public byte getTail() {
        return tail;
    }

    public void setTail(byte tail) {
        this.tail = tail;
    }
}
