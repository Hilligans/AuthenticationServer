package Hilligans;


import Hilligans.Network.PacketBase;
import Hilligans.Network.ServerNetworkInit;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import java.util.Properties;

public class Main {

    public static IDatabaseInterface database;

    //time until token expires after being generated
    public static final int TOKEN_VALID_SECONDS = 14400;
    //time required after checking if a token is valid until the method can be called again
    public static final int TOKEN_VERIFY_DELAY = 5;

    public static String port = "25588";

    public static String password = "";
    public static String email = "ourcraft892@gmail.com";

    public static Session session;

    public static void main(String[] args) throws Exception {

        String host = "localhost";
        Properties properties = System.getProperties();
        //properties.setProperty("mail.smtp.host", host);
        properties.put("mail.smtp.host", "smtp.gmail.com"); //SMTP Host
        properties.put("mail.smtp.ssl.trust", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587"); //TLS Port
        properties.put("mail.smtp.auth", "true"); //enable authentication
        properties.put("mail.smtp.starttls.enable", "true"); //enable
        session = Session.getDefaultInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(email, password);
            }
        });

        database = new RedisInterface();
        PacketBase.register();
        System.out.println("Starting Server on port: " + port);
        ServerNetworkInit.startServer(port);
    }


}
