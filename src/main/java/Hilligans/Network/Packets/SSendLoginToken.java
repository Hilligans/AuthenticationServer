package Hilligans.Network.Packets;

import Hilligans.Network.PacketBase;
import Hilligans.Network.PacketData;

public class SSendLoginToken extends PacketBase {

    String token;

    public SSendLoginToken() {
        super(3);
    }

    public SSendLoginToken(String token) {
        this();
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