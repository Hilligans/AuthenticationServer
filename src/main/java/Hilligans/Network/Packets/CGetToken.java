package Hilligans.Network.Packets;

import Hilligans.Main;
import Hilligans.Network.PacketBase;
import Hilligans.Network.PacketData;
import Hilligans.Network.ServerNetworkHandler;
import Hilligans.TokenHandler;
import Hilligans.RedisInterface;

import java.net.InetAddress;
import java.net.InetSocketAddress;

public class CGetToken extends PacketBase {

    String username;
    String loginToken;

    public CGetToken() {
        super(1);
    }

    @Override
    public void encode(PacketData packetData) {}

    @Override
    public void decode(PacketData packetData) {
        username = packetData.readString();
        loginToken = packetData.readString();
    }

    @Override
    public void handle() {
        if (Main.database.clientValid(username)) {
            String uuid = Main.database.getUUID(username);
            if(Main.database.getLoginToken(uuid).equals(loginToken)) {
                String token = TokenHandler.createNewToken(uuid, ((InetSocketAddress)ctx.channel().remoteAddress()).getAddress().getHostAddress());
                ServerNetworkHandler.sendPacket(new SSendToken(token),ctx);
            } else {
                String token = TokenHandler.getToken(64);
                Main.database.putLoginToken(uuid,token);
                ServerNetworkHandler.sendPacket(new SSendToken(""),ctx);
            }
        }
    }
}
