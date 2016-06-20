package utils;

import java.security.MessageDigest;
import java.util.HashMap;

/**
 * Created by adn55 on 16/5/19.
 */
public class UserMgr {
    public static final int RESULT_OK = 0;
    public static final int RESULT_USER_EXISTS = -2;
    public static final int RESULT_USER_NOEXIST = -3;
    public static final int RESULT_PASSWORD_INCORRECT = -4;

    private static final HashMap<String, String> sessionIds = new HashMap<>();

    public static String newSessionId(String username) {
        String sessionId = hash(System.currentTimeMillis() + "" + Math.random());
        sessionIds.put(sessionId, username);
        return sessionId;
    }

    public static int checkLogin(String username, String pwdhash) {
        if (DataMgr.data.users.containsKey(username)) {
            if (DataMgr.data.users.get(username).equals(pwdhash)) {
                return RESULT_OK;
            } else {
                return RESULT_PASSWORD_INCORRECT;
            }
        } else {
            return RESULT_USER_NOEXIST;
        }
    }

    public static String getUsernameBySessionId(String sessionId) {
        return sessionIds.get(sessionId);
    }

    public static int addUser(String username, String password) {
        if (DataMgr.data.users.containsKey(username)) {
            return RESULT_USER_EXISTS;
        } else {
            DataMgr.data.users.put(username, hash(password));
            DataMgr.saveToFile();
            return RESULT_OK;
        }
    }

    public static int delUser(String username) {
        if (DataMgr.data.users.containsKey(username)) {
            DataMgr.data.users.remove(username);
            DataMgr.saveToFile();
            return RESULT_OK;
        } else {
            return RESULT_USER_NOEXIST;
        }
    }

    public static int changePassword(String username, String oldPwdHash, String newPwdHash) {
        if (DataMgr.data.users.containsKey(username)) {
            if (DataMgr.data.users.get(username).equals(oldPwdHash)) {
                return resetPassword(username, newPwdHash);
            } else {
                return RESULT_PASSWORD_INCORRECT;
            }
        } else {
            return RESULT_USER_NOEXIST;
        }
    }

    public static int resetPassword(String username, String newPwdHash) {
        if (DataMgr.data.users.containsKey(username)) {
            DataMgr.data.users.remove(username);
            DataMgr.data.users.put(username, newPwdHash);
            DataMgr.saveToFile();
            return RESULT_OK;
        } else {
            return RESULT_USER_NOEXIST;
        }
    }

    public static String hash(String str) {
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("SHA1");
        } catch (Exception e) {
            return "";
        }
        byte[] bytes = digest.digest(str.getBytes());
        String result = "";
        for (byte b : bytes) {
            result = result + Integer.toString((b & 0xff) + 0x100, 16).substring(1);
        }
        return result;
    }
}
