package Hilligans.Network.Packets;

import Hilligans.Network.PacketBase;
import Hilligans.Network.PacketData;

public class SAccountPacket extends PacketBase {

    String response;

    public SAccountPacket(String response) {
        super(0);
        System.out.println(response);
        this.response = response;
    }

    @Override
    public void encode(PacketData packetData) {
        packetData.writeString(response);
    }

    @Override
    public void decode(PacketData packetData) {}

    @Override
    public void handle() {}
}
