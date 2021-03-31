package Hilligans.Network.Packets;

import Hilligans.Network.PacketBase;
import Hilligans.Network.PacketData;

public class SSendToken extends PacketBase {

    String token;
    public SSendToken(String token) {
        super(4);
        this.token = token;
    }

    @Override
    public void encode(PacketData packetData) {
        packetData.writeString(token);
    }

    @Override
    public void decode(PacketData packetData) {}

    @Override
    public void handle() {}
}
