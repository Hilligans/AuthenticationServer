package Hilligans.Network.Packets;

import Hilligans.Main;
import Hilligans.Network.PacketBase;
import Hilligans.Network.PacketData;
import Hilligans.Network.ServerNetworkHandler;
import Hilligans.RedisInterface;
import com.fasterxml.uuid.Generators;

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
        if(username.length() > 1) {
            if (password.length() > 1) {
                if (email != null && isValidEmailAddress(email)) {
                    if (username != null && Main.database.clientValid(username)) {
                        String uuid = Generators.randomBasedGenerator().generate().toString();
                        Main.database.putUsername(email, username);
                        Main.database.putPassword(uuid, password);
                        Main.database.putUUID(username,uuid);
                    } else {
                        ServerNetworkHandler.sendPacket(new SAccountPacket("Account already exists with that username"));
                    }
                } else {
                    ServerNetworkHandler.sendPacket(new SAccountPacket("Invalid email"));
                }
            } else {
                ServerNetworkHandler.sendPacket(new SAccountPacket("Password too short"));
            }
        } else {
            ServerNetworkHandler.sendPacket(new SAccountPacket("Username too short"));
        }
    }

    public static boolean isValidEmailAddress(String email) {
        try {
            InternetAddress emailAddr = new InternetAddress(email);
            emailAddr.validate();
        } catch (AddressException ex) {
            return false;
        }
        return true;
    }
}
