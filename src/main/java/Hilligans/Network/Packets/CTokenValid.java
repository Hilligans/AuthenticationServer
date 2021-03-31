package Hilligans.Network.Packets;

import Hilligans.Network.PacketBase;
import Hilligans.Network.PacketData;
import Hilligans.Network.ServerNetworkHandler;
import Hilligans.TokenHandler;

public class CTokenValid extends PacketBase {

    String username;
    String token;
    String ip;

    public CTokenValid() {
        super(2);
    }

    @Override
    public void encode(PacketData packetData) {}

    @Override
    public void decode(PacketData packetData) {
        username = packetData.readString();
        token = packetData.readString();
        ip = packetData.readString();
    }

    @Override
    public void handle() {
        ServerNetworkHandler.sendPacket(new STokenValid(username, TokenHandler.tokenValid(username,token,ip)));
    }
}
