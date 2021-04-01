package Hilligans.Network;

import Hilligans.Network.Packets.CCreateAccount;
import Hilligans.Network.Packets.CGetToken;
import Hilligans.Network.Packets.CTokenValid;
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
        packets.add(CCreateAccount::new);
        packets.add(CGetToken::new);
        packets.add(CTokenValid::new);

    }

}
