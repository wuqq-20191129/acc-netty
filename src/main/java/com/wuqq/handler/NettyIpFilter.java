package com.wuqq.handler;

import com.wuqq.dao.st.StLccIp;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.ipfilter.IpSubnetFilter;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Hashtable;

//import static com.wuqq.server.NettySever.ALL_LCC_IP;

/**
 * @Classname NettyIpFilter
 * @Description 过滤IP
 * @Date 2021/1/5 10:33
 * @Created by mh
 */

@Component
public class NettyIpFilter extends IpSubnetFilter {

    private static Logger logger = Logger.getLogger(NettyIpFilter.class);

    public static Hashtable ALL_LCC_IP = new Hashtable();
    public static Hashtable ALL_BOM_IP = new Hashtable();



    @Autowired
    public StLccIp stLccIp;


    public NettyIpFilter(){
    }

    @Override
    protected boolean accept(ChannelHandlerContext channelHandlerContext, InetSocketAddress inetSocketAddress) {
        String clientIp = inetSocketAddress.getAddress().getHostAddress();
        //获取IP白名单
        ArrayList<Hashtable<String,String>> arrayList =(ArrayList<Hashtable<String,String>>) stLccIp.getLccIp();
        ALL_LCC_IP =convertToMap(arrayList,"LCC_LINE_ID","LCC_IP");

        ArrayList<Hashtable<String,String>> bomList =(ArrayList<Hashtable<String,String>>) stLccIp.getBomIp();
        ALL_BOM_IP =convertToMap(bomList,"BOM","IP_ADDRESS");

        if(ALL_LCC_IP.containsValue(clientIp)||ALL_BOM_IP.containsValue(clientIp)){
            return true;
        }
        logger.info(clientIp+" 连接不合法");
        return false;
    }



    private Hashtable convertToMap(ArrayList<Hashtable<String, String>> list, String mapKey, String mapValue) {
        if (list == null || list.size() == 0) {
            return null;
        }
        Hashtable map = new Hashtable(list.size());
        for (int i = 0; i < list.size(); i++) {
            Hashtable vo = (Hashtable) list.get(i);
            String key = (String) vo.get(mapKey);
            String value = (String) vo.get(mapValue);
            map.put(key, value);
        }
        return map;
    }
}
