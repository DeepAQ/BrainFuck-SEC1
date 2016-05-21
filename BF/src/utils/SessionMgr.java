package utils;

import org.json.JSONObject;
import org.json.JSONTokener;

import java.net.URLEncoder;

/**
 * Created by adn55 on 16/5/20.
 */
public class SessionMgr {
    public static String host = "http://localhost:8081";
    public static String sessionId = "";

    // User login & logout

    public static void login(String username, String password) throws Exception {
        loginWithPwdhash(username, NetUtils.hash(password));
    }

    public static void loginWithPwdhash(String username, String pwdhash) throws Exception {
        String serverResp = NetUtils.getURL(host + "/user/login?username=" + username + "&pwdhash=" + pwdhash);
        JSONObject jsonObj = (JSONObject) new JSONTokener(serverResp).nextValue();
        int result = jsonObj.getInt("result");
        if (result < 0) {
            throw new Exception(jsonObj.getString("errmsg"));
        } else {
            sessionId = jsonObj.getString("sessid");
        }
    }

    public static void logout() {
        sessionId = "";
    }

    public static void saveLoginInfo(String username, String password) {
        DataMgr.data.host = host;
        DataMgr.data.username = username;
        DataMgr.data.pwdhash = NetUtils.hash(password);
        DataMgr.saveToFile();
    }

    public static boolean tryAutoLogin() {
        host = DataMgr.data.host;
        String username = DataMgr.data.username;
        String pwdhash = DataMgr.data.pwdhash;
        if (username.isEmpty() || pwdhash.isEmpty()) return false;
        try {
            loginWithPwdhash(username, pwdhash);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // Code execute

    public static String execute(String code, String input) throws Exception {
        String encCode = URLEncoder.encode(URLEncoder.encode(code, "utf-8"), "utf-8");
        String encInput = URLEncoder.encode(URLEncoder.encode(input, "utf-8"), "utf-8");
        String serverResp = NetUtils.getURL(host + "/io/execute?sessid=" + sessionId + "&code=" + encCode + "&input=" + encInput);
        JSONObject jsonObj = (JSONObject) new JSONTokener(serverResp).nextValue();
        if (jsonObj.getInt("result") < 0) {
            throw new Exception(jsonObj.getString("errmsg"));
        } else {
            return jsonObj.getString("output");
        }
    }

}
