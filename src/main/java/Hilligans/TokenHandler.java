package Hilligans;

import Hilligans.Main;
import Hilligans.RedisInterface;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Map;
import java.util.Random;

public class TokenHandler {

    public static final String alphanum = "ABCDEFGHIJKLMNOPQRSTUVQXYZabcdefghijklmnopqrstuvwxyz1234567890`!@#$%^&*()-_=+~[]\\;',./{}|:\"<>?;";
    private static final char[] symbols = alphanum.toCharArray();
    static Random random = new SecureRandom();
    static final int length = 64;

    public static String getToken() {
        char[] buf = new char[length];
        int salt = (int) (System.nanoTime() & 31);
        for (int idx = 0; idx < buf.length; ++idx)
            buf[idx] = getChar(Math.abs(Integer.rotateRight(random.nextInt(),salt)));
        return new String(buf);
    }

    public static String getToken(int length) {
        char[] buf = new char[length];
        int salt = (int) (System.nanoTime() & 31);
        for (int idx = 0; idx < buf.length; ++idx)
            buf[idx] = getChar(Math.abs(Integer.rotateRight(random.nextInt(),salt)));
        return new String(buf);
    }

    public static char getChar(int index) {
        return symbols[index % symbols.length];
    }

    public static boolean tokenValid(String uuid, String token, String ip) {
        if(Main.database.getStoredToken(uuid).equals(token)) {
            long time = Integer.parseInt(Main.database.getTokenDelay(uuid));
            Main.database.putTokenDelay(uuid,System.currentTimeMillis() + Main.TOKEN_VERIFY_DELAY * 1000);
            long currentTime = System.currentTimeMillis();
            if(Main.database.getIp(uuid).equals(ip)) {
                if (currentTime - time > Main.TOKEN_VERIFY_DELAY * 1000) {
                    long tokenExpire = Integer.parseInt(Main.database.getTokenTimeout(uuid));
                    return tokenExpire > currentTime;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } else {
            Main.database.putTokenDelay(uuid,System.currentTimeMillis() + Main.TOKEN_VERIFY_DELAY * 1000);
            return false;
        }
    }

    public static String createNewToken(String uuid, String ip) {
        String token = getToken();
        Main.database.putIp(uuid,ip);
        Main.database.putToken(uuid,token);
        Main.database.putTokenTimeout(uuid, System.currentTimeMillis() + Main.TOKEN_VALID_SECONDS * 1000);
        Main.database.putTokenDelay(uuid,System.currentTimeMillis() + Main.TOKEN_VERIFY_DELAY * 1000);
        return token;
    }



}
