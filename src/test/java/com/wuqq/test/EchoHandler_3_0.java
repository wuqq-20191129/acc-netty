package com.wuqq.test;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class EchoHandler_3_0 extends ChannelInboundHandlerAdapter {

    //连接成功后发送消息测试
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        //User user = new User();
        //user.setBirthday(new Date());
        //user.setUID(UUID.randomUUID().toString());
        //user.setName("冉鹏峰");
        //user.setAge(24);
        //Map<String,User> map=new HashMap<>();
        //map.put("数据一",user);
        //List<User> users=new ArrayList<>();
        //users.add(user);
        //TcpProtocol_3_0 protocol = ProtocolUtils.prtclInstance(map,user.getClass().getName());
        ////传map
        //ctx.write(protocol);//由于设置了编码器，这里直接传入自定义的对象
        //ctx.flush();
        ////传list
        //ctx.write(ProtocolUtils.prtclInstance(users,user.getClass().getName()));
        //ctx.flush();
        ////传单一实体
        //ctx.write(ProtocolUtils.prtclInstance(user));
        //ctx.flush();
//        byte[] msg = {(byte)0xeb,0x01,0,0,0,0x03};
//        int i =254;
//        while(i>0){
//            msg[2] = (byte)i;
//            ctx.write(msg);
//            ctx.flush();
//            i--;
//        }

    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//        super.channelRead(ctx, msg);
        if(msg instanceof byte[]){
            ctx.writeAndFlush((byte[])msg);
        }
    }
}
