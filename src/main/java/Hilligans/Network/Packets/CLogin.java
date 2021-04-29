package Hilligans.Network.Packets;

import Hilligans.Main;
import Hilligans.Network.PacketBase;
import Hilligans.Network.PacketData;
import Hilligans.Network.ServerNetworkHandler;
import Hilligans.TokenHandler;

import java.net.InetSocketAddress;

public class CLogin extends PacketBase {

    String username;
    String password;
    String email;


    public CLogin() {
        super(3);
    }

    @Override
    public void encode(PacketData packetData) {}

    @Override
    public void decode(PacketData packetData) {
        username = packetData.readString();
        password = packetData.readString();
        email = packetData.readString();
    }

    @Override
    public void handle() {
        String uuid = Main.database.getUUID(username);
        if(Main.database.passwordValid(uuid,password)) {
            if(Main.database.getUsername(email).equals(username)) {
                String token = TokenHandler.getToken(64);
                Main.database.putLoginToken(uuid, token);
                ServerNetworkHandler.sendPacket(new SSendLoginToken(token), ctx);
            }
        }
    }
}