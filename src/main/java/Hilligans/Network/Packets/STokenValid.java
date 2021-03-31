package Hilligans.Network.Packets;

import Hilligans.Network.PacketBase;
import Hilligans.Network.PacketData;

public class STokenValid extends PacketBase {

    String username;
    boolean valid;

    public STokenValid(String username, boolean valid) {
        super(5);
        this.username = username;
        this.valid = valid;
    }

    @Override
    public void encode(PacketData packetData) {
        packetData.writeString(username);
        packetData.writeBoolean(valid);
    }

    @Override
    public void decode(PacketData packetData) {}

    @Override
    public void handle() {}
}
