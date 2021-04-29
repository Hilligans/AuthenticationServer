package Hilligans;


import Hilligans.Database.JedisInitializer;
import Hilligans.Network.PacketBase;
import Hilligans.Network.ServerNetworkInit;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
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
        ArrayList<String> data = readShader("/data.txt");
        String pathToJedis = "";
        if(data != null && data.size() >= 2) {
            password = data.get(0);
            pathToJedis = data.get(1);
        }

        Properties properties = System.getProperties();
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
        RedisInterface.jedis = new JedisInitializer("win10","redis-server.exe",pathToJedis);
        PacketBase.register();
        System.out.println("Starting Server on port: " + port);
        ServerNetworkInit.startServer(port);
    }

    public static ArrayList<String> readShader(String source) {
        InputStream stream = Main.class.getResourceAsStream(source);
        if(stream == null) {
            return null;
        }
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        ArrayList<String> strings = new ArrayList<>();
        reader.lines().forEach(strings::add);
        return strings;
    }



}
