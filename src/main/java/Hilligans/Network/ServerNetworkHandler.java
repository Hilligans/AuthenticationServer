package Hilligans.Network;

import io.netty.channel.*;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.ssl.SslHandler;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.util.ArrayList;
import java.util.HashMap;

public class ServerNetworkHandler extends SimpleChannelInboundHandler<PacketData> {

    static final ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    static ArrayList<ChannelId> channelIds = new ArrayList<>();


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.pipeline().get(SslHandler.class).handshakeFuture().addListener(
                (GenericFutureListener<Future<Channel>>) future -> {
                    channels.add(ctx.channel());
                    channelIds.add(ctx.channel().id());
                });
        super.channelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        channelIds.remove(ctx.channel().id());
        super.channelInactive(ctx);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, PacketData msg) throws Exception {
        try {
            PacketBase packetBase = msg.createPacket();
            packetBase.handle();
        } catch (Exception ignored) {
            ignored.printStackTrace();
        }
    }

    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

    public static ChannelFuture sendPacket(PacketBase packetBase) {
        for (int x = 0; x < channelIds.size(); x++) {
            Channel channel = channels.find(channelIds.get(x));
            if (channel == null) {
                channelIds.remove(x);
                x--;
                continue;
            }
            return channel.writeAndFlush(new PacketData(packetBase));
        }
        return null;
    }

    public static ChannelFuture sendPacket(PacketBase packetBase, ChannelHandlerContext ctx) {
        return ctx.channel().writeAndFlush(new PacketData(packetBase)).addListeners((ChannelFutureListener) future -> {
            if (ctx.channel().isOpen()) {
                ctx.channel().close().awaitUninterruptibly(100);
            }
        });
    }

    public static void sendPacket(PacketBase packetBase, ChannelId channelId) {
        channels.find(channelId).writeAndFlush(new PacketData(packetBase));
    }

}