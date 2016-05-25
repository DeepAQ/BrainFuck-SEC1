package utils;

import javax.net.ssl.HttpsURLConnection;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;

/**
 * Created by adn55 on 16/5/21.
 */
public class NetUtils {
    public static String getURL(String url) throws IOException {
        URLConnection _conn = new URL(url).openConnection();
        _conn.setConnectTimeout(5000);
        _conn.setReadTimeout(5000);
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
