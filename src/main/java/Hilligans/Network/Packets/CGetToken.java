package Hilligans.Network.Packets;

import Hilligans.Network.PacketBase;
import Hilligans.Network.PacketData;
import Hilligans.Network.ServerNetworkHandler;
import Hilligans.TokenHandler;
import Hilligans.RedisInterface;

import java.net.InetAddress;
import java.net.InetSocketAddress;

public class CGetToken extends PacketBase {

    String username;
    String password;

    public CGetToken() {
        super(1);
    }

    @Override
    public void encode(PacketData packetData) {}

    @Override
    public void decode(PacketData packetData) {
        username = packetData.readString();
        password = packetData.readString();
    }

    @Override
    public void handle() {
        if(RedisInterface.passwordValid(username,password)) {
            String token = TokenHandler.createNewToken(username, ((InetSocketAddress)ctx.channel().remoteAddress()).getHostName());
            ServerNetworkHandler.sendPacket(new SSendToken(token),ctx);
        }
    }
}
