package Hilligans;

import at.favre.lib.crypto.bcrypt.BCrypt;
import redis.clients.jedis.Jedis;

public class RedisInterface {

    static Jedis jedis = new Jedis();

    public static synchronized void putString(String key, String value) {
        jedis.set(key,value);
    }

    public static synchronized void putToken(String username, String token) {
        jedis.set('T' + username,token);
    }

    public static synchronized void putTokenDelay(String username, long delay) {
        jedis.set('D' + username,Long.toString(delay));
    }

    public static synchronized void putTokenTimeout(String username, long timeout) {
        jedis.set('O' + username,Long.toString(timeout));
    }

    public static void putPassword(String username, String password) {
        putString('P' + username, hashString(password));
    }

    public static void putIp(String username, String ip) {
        jedis.set('I' + username, ip);
    }

    public static void putUsername(String email, String username) {
        jedis.set('E' + email, username);
    }

    public static synchronized String getUsername(String email) {
        return jedis.get('E' + email);
    }

    public static synchronized String getPassword(String username) {
        return jedis.get('P' + username);
    }

    public static synchronized String getTokenDelay(String username) {
        return jedis.get('D' + username);
    }

    public static synchronized String getTokenTimeout(String username) {
        return jedis.get('O' + username);
    }

    public static synchronized String getStoredToken(String username) {
        return jedis.get("T" + username);
    }

    public static synchronized String getIp(String username) {
        return jedis.get('I' + username);
    }

    public static synchronized boolean clientValid(String username) {
        return  getPassword(username) != null;
    }

    public static boolean passwordValid(String username, String password) {
        if(!clientValid(username)) {
            return false;
        }
        String storedPassword = getPassword(username);
        String bcryptHashString = hashString(password);
        BCrypt.Result result = BCrypt.verifyer().verify(storedPassword.toCharArray(), bcryptHashString);

        return result.verified;
    }

    public static String hashString(String password) {
        return BCrypt.withDefaults().hashToString(12, password.toCharArray());
    }

    public static void stop() {
        jedis.close();
    }

}
