package com.wuqq.base;

import com.wuqq.constant.message.Message;
import com.wuqq.exception.MessageException;
import org.apache.log4j.Logger;

import java.io.*;
import java.util.Date;
import java.util.Iterator;
import java.util.Vector;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Iterator;
import java.util.Vector;

import org.apache.log4j.Logger;

//import com.goldsign.commu.frame.constant.FrameLogConstant;
//import com.goldsign.commu.frame.dao.ComMessageQueueDao;
//import com.goldsign.commu.frame.exception.MessageException;
//import com.goldsign.commu.frame.util.ByteGenUtil;
//import com.goldsign.commu.frame.util.LogDbUtil;
//import com.goldsign.commu.frame.vo.MessageQueue;


/**
 * @Classname ConstructMessageBase
 * @Description TODO
 * @Date 2021/1/19 11:15
 * @Created by mh
 */


public class ConstructMessageBase {

    /**
     * 日志记录使用
     */
    private long hdlStartTime; // 处理的起始时间
    private long hdlEndTime;// 处理的结束时间

    public String messageType = "";
    public String messageTypeSub = "";
    public String messageRemark = "";

    private final int maxLength = 65536;
    private final String NotParaInformMsg = "0";
    private byte[] message;
    private int pointer;
    protected final static int CRLF_LENGTH = 6;
    protected final static char[] CRLF = {0x0d, 0x0a, 0x0d, 0x0a, 0x0d, 0x0a};
    public static int T_BCD = 2;
    public static int T_INT = 3;
    public static int T_STR = 1;
    protected final static String SVER = "CRC:";
    private final byte STX_B = (byte) 0xEB;
    private final byte ETX = 0x03;
    private final byte QRY = 0x01;
    private final byte[] QUERY = { STX_B, QRY, 0, 0, 0, ETX };
    private static Logger logger = Logger.getLogger(ConstructMessageBase.class
            .getName());

    public ConstructMessageBase() {
    }

    public void handle() {
    }



    /*
    取line_id
    */
    //private String getLineIdFromMap(String ip){
    //    String lineId = "00";
    //    Iterator<String> iterator = FrameCodeConstant.ALL_LCC_IP.keySet().iterator();
    //    while (iterator.hasNext()) {
    //        String key = iterator.next();
    //        String value = FrameCodeConstant.ALL_LCC_IP.get(key);
    //        if(value.equals(ip)){
    //            lineId = key;
    //        }
    //    }
    //    return lineId;
    //}

    /**
     * len is the bytes length after transform
     */
//    protected void AddStringToMessage(String str, int len) {
//        byte[] b = str.getBytes();
//        AddByteArrayToMessage(b, len);
//    }

    /**
     * len is the bytes length after transform
     * 不足20位时左对齐，右补空格。默认值：全为0
     */
    protected void AddStringToMessage(String str, int len) {
        if (str.length() >= len) {
            str = str.substring(0, len);
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append(str);
            for (int i = 0; i < len - str.length(); i++) {
                sb.append(" ");
            }
            str = sb.toString();
        }

        byte[] b = str.getBytes();
        AddByteArrayToMessage(b, len);
    }

    /**
     * len is the bytes length after transform
     */
    protected void AddBcdToMessage(String str, int len) throws MessageException {
        byte[] b = bcdStringToByteArray(str);
        if (b == null) {
            throw new MessageException("BCD transform error!");
        }
        AddByteArrayToMessage(b, len);
    }

    /**
     * len is the bytes length after transform
     */
    protected void AddIntToMessage(int i, int len) throws MessageException {
        if (i > 256) {
            throw new MessageException("Message repeat count > 256! ");
        }
        byte[] b = {(byte) i};
        AddByteArrayToMessage(b, 1);
    }

    protected void AddByteArrayToMessage(byte[] b, int len) {
        if (len > b.length) {
            System.arraycopy(b, 0, message, pointer, b.length);
            pointer = pointer + b.length;
            // char space = ' ';
            byte[] space = {0x20};
            for (int i = (len - b.length); i > 0; i--) {
                System.arraycopy(space, 0, message, pointer, 1);
                pointer = pointer + 1;
            }
        } else {
            System.arraycopy(b, 0, message, pointer, len);
            pointer = pointer + len;
        }
    }

    // delete unused byte[] in the latter
    protected byte[] trimMessage() {
        byte[] result = new byte[pointer];
        System.arraycopy(message, 0, result, 0, pointer);
        return result;
    }

    protected void initMessage() {
        this.message = new byte[maxLength];
        this.pointer = 0;
    }

    // when transform 2 decimal number for example 98,run this method to get
    // 0x98
    private byte bcd2ToByte1(int i) {
        return (byte) (i / 10 * 16 + i % 10);
    }

    // run this method example : transform "123456789" to
    // {0x01,0x23,0x45,0x67,0x89}
    private byte[] bcdStringToByteArray(String str) {
        try {
            int len = str.length();
            if (str.length() == 0) {
                return null;
            }
            if (len % 2 == 1) {
                str = "0" + str;
                len = len + 1;
            }
            if (len / 2 > maxLength) {
                throw new MessageException("Transform string to BCD length > "
                        + maxLength);
            }
            byte[] tmp = new byte[maxLength];
            int p = 0;
            for (int i = 0; i < len; i = i + 2) {
                int value = Integer.parseInt(str.substring(i, i + 2));
                byte[] b = {bcd2ToByte1(value)};
                System.arraycopy(b, 0, tmp, p, 1);
                p = p + 1;
            }

            byte[] bb = new byte[p];
            System.arraycopy(tmp, 0, bb, 0, p);
            return bb;
        } catch (Exception e) {
            logger.error(e);
            return null;
        }
    }

    protected void addIntValueToCharArray(int n, char[] arr, int offset, int len) {
        String hexStr = Integer.toHexString(n);
        int lenHex = hexStr.length();
        while (lenHex < 2 * len) {
            hexStr = "0" + hexStr;
            // hexStr = hexStr+"0";
            lenHex = hexStr.length();
        }
        int j = 0;
        for (int i = 0; i < len; i++) {
            arr[offset + i] = (char) Integer.parseInt(
                    hexStr.substring(j, j + 2), 16);
            j = j + 2;

        }

    }

    /**
     *
     * @param str
     * @param arr
     * @param offset
     */
    protected void addStringToCharArray(String str, char[] arr, int offset,
                                        int len) throws MessageException {

        char[] strArr = str.toCharArray();
        for (char c : strArr) {
            arr[offset] = c;
            offset++;
        }
        strArr = null;
    }

    protected void addIntStrToCharArray(int n, char[] arr, int offset, int len)
            throws MessageException {
        String hexStr = Integer.toHexString(n);
        int hexLen = hexStr.length();
        while (hexLen < len) {
            hexStr = "0" + hexStr;
            hexLen = hexStr.length();
        }
        this.addStringToCharArray(hexStr, arr, offset, len);

    }

    /**
     * BCD转char数组
     *
     * @param src
     * @param offset
     * @param arr
     * @param len
     * @throws MessageException
     */
    protected void addBcdStrToCharArray(String src, char[] arr, int offset,
                                        int len) throws MessageException {

        int length = src.length();
        if (src.length() == 0) {
            // return null;
        }
        if (length % 2 == 1) {
            src = "0" + src;
            length = length + 1;
        }

        if (length / 2 > maxLength) {
            throw new MessageException("Transform string to BCD length > "
                    + maxLength);
        }

        // char[] tmp = new char[1024];
        // int p = 0;
        for (int i = 0; i < length; i = i + 2) {
            byte value = Byte.parseByte(src.substring(i, i + 2));
            char dd = (char) value;
            // char[] b = { dd };
            arr[offset] = dd;
            offset++;
            // System.arraycopy(b, 0, tmp, p, 1);
            // p = p + 1;
        }

        // char[] bb = new char[p];
        // System.arraycopy(tmp, 0, bb, 0, p);

    }

    protected byte[] getBytes(char[] cs) {
        byte[] bs = new byte[cs.length];
        int i = 0;
        for (char c : cs) {
            bs[i++] = (byte) c;
        }
        return bs;
    }

    protected void addCharArrayToBuffer(char[] buf, char[] ca, int offset) {
        for (char c : ca) {
            buf[offset] = c;
            offset++;
        }
    }

    protected void close(OutputStream os) {
        try {
            if (os != null) {
                os.close();
            }
        } catch (IOException ex) {
        }
    }

    protected int getBufferLen(int[] dataLen) {
        int n = 0;
        for (int i : dataLen) {
            n += i;
        }
        n = n + CRLF_LENGTH;
        return n;
    }
    protected int getBufferLenWithoutLineDelimiter(int[] dataLen) {
        int n = 0;
        for (int i : dataLen) {
            n += i;
        }
        //n = n + CRLF_LENGTH;
        return n;
    }

    public void writeFile(char[] bs, String path, String fileName) {
        String fileNameFull = path + "/" + fileName;
        File f = new File(fileNameFull);
        FileOutputStream os = null;
        DataOutputStream dos = null;
        try {
            os = new FileOutputStream(f);
            dos = new DataOutputStream(os);
            for (char c : bs) {
                dos.write((int) c);
            }
            dos.flush();
        } catch (Exception ex) {
        } finally {
            this.close(dos);
        }
    }

    //protected char[] getData(Object obj, int len, int type)
    //        throws UnsupportedEncodingException {
    //    char[] cs = null;
    //    if (type == T_STR) {
    //        cs = ByteGenUtil.stringToChar(((String) obj).trim(), len);
    //        return cs;
    //    }
    //    if (type == T_BCD) {
    //        cs = ByteGenUtil.stringBcdToChar(((String) obj).trim(), len);
    //        return cs;
    //    }
    //    if (type == T_INT) {
    //        int value = 0;
    //        if (obj instanceof String) {
    //            value = Integer.parseInt(((String) obj).trim());
    //        } else {
    //            value = ((Integer) obj).intValue();
    //        }
    //        cs = ByteGenUtil.intToChar(value, len);
    //        return cs;
    //    }
    //    return cs;
    //}

    //protected char[] getLine(Object[] datas, int[] data_len, int[] data_type)
    //        throws UnsupportedEncodingException {
    //    int blen = this.getBufferLen(data_len);
    //    char[] bs = new char[blen];
    //    Object obj;
    //    int dLen;
    //    int dType;
    //    char[] cs;
    //    int offset = 0;
    //    for (int i = 0; i < datas.length; i++) {
    //        obj = datas[i];
    //        dLen = data_len[i];
    //        dType = data_type[i];
    //        cs = this.getData(obj, dLen, dType);
    //        this.addCharArrayToBuffer(bs, cs, offset);
    //        offset += dLen;
    //    }
    //    this.addCharArrayToBuffer(bs, CRLF, offset);
    //    return bs;
    //}
    //protected char[] getLineWithoutLineDelimiter(Object[] datas, int[] data_len, int[] data_type)
    //        throws UnsupportedEncodingException {
    //    // int blen = this.getBufferLen(data_len);
    //    int blen = this.getBufferLenWithoutLineDelimiter(data_len);
    //    char[] bs = new char[blen];
    //    Object obj;
    //    int dLen;
    //    int dType;
    //    char[] cs;
    //    int offset = 0;
    //    for (int i = 0; i < datas.length; i++) {
    //        obj = datas[i];
    //        dLen = data_len[i];
    //        dType = data_type[i];
    //        cs = this.getData(obj, dLen, dType);
    //        this.addCharArrayToBuffer(bs, cs, offset);
    //        offset += dLen;
    //    }
    //    // this.addCharArrayToBuffer(bs, CRLF, offset);
    //    return bs;
    //}

    protected void AddLongToMessage(long input, int len)
            throws MessageException {
        String des = Integer.toHexString((int) input);
        if (des.length() >= 8) {
            des = des.substring(0, 7);
        } else {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < len * 2 - des.length(); i++) {
                sb.append("0");
            }
            sb.append(des);
            des = sb.toString();
        }
        byte[] b = longStringToByteArray(des);
        if (b == null) {
            throw new MessageException("Long transform error!");
        }
        AddByteArrayToMessage(b, len);
    }

    private byte[] longStringToByteArray(String des) {
        byte[] rst = new byte[4];
        rst[0] = (byte) (Integer.valueOf(des.substring(6), 16).intValue());
        rst[1] = (byte) (Integer.valueOf(des.substring(4, 6), 16).intValue());
        rst[2] = (byte) (Integer.valueOf(des.substring(2, 4), 16).intValue());
        rst[3] = (byte) (Integer.valueOf(des.substring(0, 2), 16).intValue());
        return rst;
    }

    //public Message constructQuery(int seriaNo) {
    //    Message query = new Message();
    //    query.setBegin( (byte) 0xEB);
    //    query.setDataTyeo((byte) 0x01);
    //    query.setSerialNo((byte) seriaNo);
    //    query.setEnd((byte) 0x03);
    //    return query;
    //}
    public byte[] constructQuery(int searialNo){
        QUERY[2] = (byte) searialNo;
        //logger.info(QUERY);
        return QUERY;
    }
}

