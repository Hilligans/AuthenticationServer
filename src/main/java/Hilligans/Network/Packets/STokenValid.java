package Hilligans.Network.Packets;

import Hilligans.Network.PacketBase;
import Hilligans.Network.PacketData;

public class STokenValid extends PacketBase {

    String username;
    String uuid;
    boolean valid;

    public STokenValid(String username, String uuid, boolean valid) {
        super(2);
        this.username = username;
        this.uuid = uuid;
        this.valid = valid;
    }

    @Override
    public void encode(PacketData packetData) {
        packetData.writeString(username);
        packetData.writeString(uuid);
        packetData.writeBoolean(valid);
    }

    @Override
    public void decode(PacketData packetData) {}

    @Override
    public void handle() {}
}
