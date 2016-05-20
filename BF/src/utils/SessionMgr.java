package utils;

import org.json.JSONObject;
import org.json.JSONTokener;

import javax.net.ssl.HttpsURLConnection;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;

/**
 * Created by adn55 on 16/5/20.
 */
public class SessionMgr {
    public static String host = "http://localhost:8081";
    public static String sessionId = "";

    public static void login(String username, String password) throws Exception {
        String serverResp = getURL(host + "/user/login?username=" + username + "&pwdhash=" + hash(password));
        JSONObject tmpObj = (JSONObject) new JSONTokener(serverResp).nextValue();
        int result = tmpObj.getInt("result");
        if (result < 0) {
            String errorMsg = tmpObj.getString("errmsg");
            throw new Exception(errorMsg);
        } else {
            sessionId = tmpObj.getString("sessid");
        }
    }

    public static void logout() {
        sessionId = "";
    }

    public static String getURL(String url) throws IOException {
        URLConnection _conn = new URL(url).openConnection();
        _conn.setConnectTimeout(10000);
        _conn.setReadTimeout(10000);
        String resp;
        if (_conn instanceof HttpsURLConnection) {
            HttpsURLConnection conn = (HttpsURLConnection) _conn;
            resp = getStringFromInputStream(conn.getInputStream());
            conn.disconnect();
        } else {
            HttpURLConnection conn = (HttpURLConnection) _conn;
            resp = getStringFromInputStream(conn.getInputStream());
            conn.disconnect();
        }
        return resp;
    }

    public static String getStringFromInputStream(InputStream is) throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len;
        while ((len = is.read(buffer)) != -1) {
            os.write(buffer, 0, len);
        }
        is.close();
        String state = os.toString();
        os.close();
        return state;
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
