package Hilligans;

import at.favre.lib.crypto.bcrypt.BCrypt;

public interface IDatabaseInterface {

    void putString(String key, String value);

    void putToken(String uuid, String token);

    void putTokenDelay(String uuid, long delay);

    void putTokenTimeout(String uuid, long timeout);

    void putPassword(String uuid, String password);

    void putIp(String uuid, String ip);

    void putUsername(String email, String username);

    void putUUID(String username, String uuid);

    void putEmailToken(String username, String token);

    void putLoginToken(String uuid, String token);

    String getUsername(String email);

    String getPassword(String uuid);

    String getTokenDelay(String uuid);

    String getTokenTimeout(String uuid);

    String getStoredToken(String uuid);

    String getIp(String uuid);

    String getUUID(String username);

    String getEmailToken(String username);

    String getLoginToken(String uuid);

    boolean clientValid(String username);

    void stop();

    default boolean passwordValid(String uuid, String password) {
        if(getPassword(uuid) == null) {
            return false;
        }
        String storedPassword = getPassword(uuid);
        BCrypt.Result result = BCrypt.verifyer().verify(password.toCharArray(), storedPassword);

        return result.verified;
    }

    default String hashString(String password) {
        return BCrypt.withDefaults().hashToString(12, password.toCharArray());
    }


}
