package Hilligans.Network.Packets;

import Hilligans.Network.PacketBase;
import Hilligans.Network.PacketData;
import Hilligans.Network.ServerNetworkHandler;
import Hilligans.RedisInterface;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

public class CCreateAccount extends PacketBase {

    String username;
    String password;
    String email;

    public CCreateAccount() {
        super(0);
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
        if(password.length() > 1) {
            if (email != null && isValidEmailAddress(email)) {
                if (username != null && RedisInterface.clientValid(username)) {
                    RedisInterface.putUsername(email, username);
                    RedisInterface.putPassword(username, password);
                } else {
                    ServerNetworkHandler.sendPacket(new SAccountPacket("Account already exists with that username"));
                }
            } else {
                ServerNetworkHandler.sendPacket(new SAccountPacket("Invalid email"));
            }
        } else {
            ServerNetworkHandler.sendPacket(new SAccountPacket("Password too short"));
        }
    }

    public static boolean isValidEmailAddress(String email) {
        boolean result = true;
        try {
            InternetAddress emailAddr = new InternetAddress(email);
            emailAddr.validate();
        } catch (AddressException ex) {
            result = false;
        }
        return result;
    }
}
