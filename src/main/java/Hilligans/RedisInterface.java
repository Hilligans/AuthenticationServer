package Hilligans;

import at.favre.lib.crypto.bcrypt.BCrypt;
import redis.clients.jedis.Jedis;

public class RedisInterface implements IDatabaseInterface {

    static Jedis jedis = new Jedis();

    public synchronized void putString(String key, String value) {
        jedis.set(key,value);
    }

    public synchronized void putToken(String uuid, String token) {
        jedis.set('T' + uuid,token);
    }

    public synchronized void putTokenDelay(String uuid, long delay) {
        jedis.set('D' + uuid,Long.toString(delay));
    }

    public synchronized void putTokenTimeout(String uuid, long timeout) {
        jedis.set('O' + uuid,Long.toString(timeout));
    }

    public synchronized void putPassword(String uuid, String password) {
        putString('P' + uuid, hashString(password));
    }

    public synchronized void putIp(String uuid, String ip) {
        jedis.set('I' + uuid, ip);
    }

    public synchronized void putUsername(String email, String username) {
        jedis.set('E' + email, username);
    }

    public synchronized void putUUID(String username, String uuid) {
        jedis.set('U' + uuid, uuid);
    }

    public synchronized String getUsername(String email) {
        return jedis.get('E' + email);
    }

    public synchronized String getPassword(String uuid) {
        return jedis.get('P' + uuid);
    }

    public synchronized String getTokenDelay(String uuid) {
        return jedis.get('D' + uuid);
    }

    public synchronized String getTokenTimeout(String uuid) {
        return jedis.get('O' + uuid);
    }

    public synchronized String getStoredToken(String uuid) {
        return jedis.get("T" + uuid);
    }

    public synchronized String getIp(String uuid) {
        return jedis.get('I' + uuid);
    }

    public synchronized String getUUID(String username) {
        return jedis.get('U' + username);
    }

    public synchronized boolean clientValid(String username) {
        return getUUID(username) != null;
    }

    public void stop() {
        jedis.close();
    }

}
