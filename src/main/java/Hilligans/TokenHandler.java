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

    public static char getChar(int index) {
        return symbols[index % symbols.length];
    }

    public static boolean  tokenValid(String username, String token, String ip) {
        if(RedisInterface.clientValid(username) && RedisInterface.getStoredToken(username).equals(token)) {
            long time = Integer.parseInt(RedisInterface.getTokenDelay(username));
            RedisInterface.putTokenDelay(username,System.currentTimeMillis() + Main.TOKEN_VERIFY_DELAY * 1000);
            long currentTime = System.currentTimeMillis();
            if(RedisInterface.getIp(username).equals(ip)) {
                if (currentTime - time > Main.TOKEN_VERIFY_DELAY * 1000) {
                    long tokenExpire = Integer.parseInt(RedisInterface.getTokenTimeout(username));
                    return tokenExpire > currentTime;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } else {
            RedisInterface.putTokenDelay(username,System.currentTimeMillis() + Main.TOKEN_VERIFY_DELAY * 1000);
            return false;
        }
    }

    public static String createNewToken(String username, String ip) {
        String token = getToken();
        RedisInterface.putIp(username,ip);
        RedisInterface.putToken(username,token);
        RedisInterface.putTokenTimeout(username, System.currentTimeMillis() + Main.TOKEN_VALID_SECONDS * 1000);
        RedisInterface.putTokenDelay(username,System.currentTimeMillis() + Main.TOKEN_VERIFY_DELAY * 1000);
        return token;
    }



}