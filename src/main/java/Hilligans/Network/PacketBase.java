package Hilligans.Network;

import io.netty.channel.ChannelHandlerContext;

import java.util.ArrayList;

public abstract class PacketBase {

    public static ArrayList<PacketFetcher> packets = new ArrayList<>();

    public ChannelHandlerContext ctx;


    public int packetId;

    public PacketBase(int id) {
        this.packetId = id;
    }

    public abstract void encode(PacketData packetData);

    public abstract void decode(PacketData packetData);

    public abstract void handle();

    public static void register() {


    }

}
