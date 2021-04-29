package Hilligans.Network.Packets;

import Hilligans.Main;
import Hilligans.Network.PacketBase;
import Hilligans.Network.PacketData;
import Hilligans.Network.ServerNetworkHandler;
import Hilligans.TokenHandler;
import com.fasterxml.uuid.Generators;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class CCreateAccount extends PacketBase {

    String username;
    String password;
    String email;
    String verificationToken;

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
        verificationToken = packetData.readString();
    }

    @Override
    public void handle() {
        createAccount(this);
    }

    public static synchronized void createAccount(CCreateAccount data) {
        if(data.username.length() > 1) {
            if(data.username.length() < 24) {
                if (data.password.length() > 1) {
                    if (data.email != null && isValidEmailAddress(data.email)) {
                        if (data.username != null && !Main.database.clientValid(data.username)) {
                            String verificationToken = Main.database.getEmailToken(data.username);
                            if (verificationToken != null && verificationToken.equals(data.verificationToken)) {
                                String uuid = Generators.randomBasedGenerator().generate().toString();
                                Main.database.putUsername(data.email, data.username);
                                Main.database.putPassword(uuid, data.password);
                                Main.database.putUUID(data.username, uuid);
                                String loginToken = TokenHandler.getToken(64);
                                Main.database.putLoginToken(uuid, loginToken);
                                ServerNetworkHandler.sendPacket(new SSendLoginToken(loginToken), data.ctx);
                                ServerNetworkHandler.sendPacket(new SAccountPacket("Account created successfully"), data.ctx);
                            } else {
                                String token = TokenHandler.getToken(16);
                                Main.database.putEmailToken(data.username, token);
                                try {
                                    MimeMessage message = new MimeMessage(Main.session);
                                    message.setFrom(new InternetAddress("hilligans10@gmail.com"));
                                    message.addRecipient(Message.RecipientType.TO, new InternetAddress(data.email));
                                    message.setSubject("Account Creation");
                                    message.setText("Token:" + token);
                                    Transport.send(message);
                                    ServerNetworkHandler.sendPacket(new SAccountPacket("An email has been sent to you with a token please enter it into the token box"), data.ctx);
                                } catch (MessagingException mex) {
                                    mex.printStackTrace();
                                }
                            }
                        } else {
                            ServerNetworkHandler.sendPacket(new SAccountPacket("Account already exists with that username"), data.ctx);
                        }
                    } else {
                        ServerNetworkHandler.sendPacket(new SAccountPacket("Invalid email"), data.ctx);
                    }
                } else {
                    ServerNetworkHandler.sendPacket(new SAccountPacket("Password too short"), data.ctx);
                }
            }  else {
                ServerNetworkHandler.sendPacket(new SAccountPacket("Username too long"), data.ctx);
            }
        } else {
            ServerNetworkHandler.sendPacket(new SAccountPacket("Username too short"),data.ctx);
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
