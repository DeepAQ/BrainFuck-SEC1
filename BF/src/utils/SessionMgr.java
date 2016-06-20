package utils;

import org.json.JSONObject;
import org.json.JSONTokener;

import java.net.URLEncoder;

/**
 * Created by adn55 on 16/5/20.
 */
public class SessionMgr {
    public static String host = "http://localhost:8081";
    public static String username = "";
    private static String sessionId = "";

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
            SessionMgr.username = username;
        }
    }

    public static void logout() {
        username = "";
        sessionId = "";
        DataMgr.data.username = "";
        DataMgr.data.pwdhash = "";
        DataMgr.saveToFile();
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

    public static void changePassword(String oldPassword, String newPassword) throws Exception {
        String serverResp = NetUtils.getURL(host + "/user/changepassword?sessid=" + sessionId + "&oldpwdhash=" + NetUtils.hash(oldPassword) + "&newpwdhash=" + NetUtils.hash(newPassword));
        JSONObject jsonObj = (JSONObject) new JSONTokener(serverResp).nextValue();
        int result = jsonObj.getInt("result");
        if (result < 0) {
            throw new Exception(jsonObj.getString("errmsg"));
        }
    }

    // File I/O

    public static String getFileList() throws Exception {
        return NetUtils.getURL(host + "/io/list?sessid=" + sessionId);
    }

    public static String getFileContent(String filename, String version) throws Exception {
        String serverResp = NetUtils.getURL(host + "/io/open?sessid=" + sessionId + "&filename=" + filename + "&version=" + version);
        JSONObject jsonObj = (JSONObject) new JSONTokener(serverResp).nextValue();
        if (jsonObj.getInt("result") < 0) {
            throw new Exception(jsonObj.getString("errmsg"));
        } else {
            return jsonObj.getString("code");
        }
    }

    public static String saveFile(String code, String filename) throws Exception {
        String encCode = URLEncoder.encode(URLEncoder.encode(code, "utf-8"), "utf-8");
        String serverResp = NetUtils.getURL(host + "/io/save?sessid=" + sessionId + "&code=" + encCode + "&filename=" + filename);
        JSONObject jsonObj = (JSONObject) new JSONTokener(serverResp).nextValue();
        if (jsonObj.getInt("result") < 0) {
            throw new Exception(jsonObj.getString("errmsg"));
        } else {
            return jsonObj.getString("version");
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
