package serviceImpl;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.json.JSONStringer;
import utils.LogUtils;
import utils.UserMgr;

import java.io.*;

/**
 * Created by adn55 on 16/5/26.
 */
public class FileOpenHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String query = httpExchange.getRequestURI().getQuery();
        LogUtils.log("调试", "文件打开处理程序", "收到新的打开文件请求");
        String sessid = "";
        String filename = "";
        String version = "";
        if (query != null) {
            for (String param : query.split("&")) {
                String pair[] = param.split("=");
                if (pair.length > 1) {
                    switch (pair[0]) {
                        case "filename":
                            filename = pair[1];
                            break;
                        case "version":
                            version = pair[1];
                            break;
                        case "sessid":
                            sessid = pair[1];
                            break;
                    }
                }
            }
        }
        String username = UserMgr.getUsernameBySessionId(sessid);
        if (username == null || filename == null || version == null) {
            httpExchange.sendResponseHeaders(403, 0);
            httpExchange.close();
            return;
        }

        JSONStringer json = new JSONStringer();
        json.object();
        try {
            String path = "objects/" + username + "/" + filename;
            InputStreamReader reader = new InputStreamReader(
                    new FileInputStream(path + "/" + version + ".bf"),
                    "utf-8"
            );
            char[] buffer = new char[1];
            StringBuilder code = new StringBuilder();
            while (reader.read(buffer) != -1) {
                code.append(buffer);
            }
            json.key("result").value(0);
            json.key("code").value(code.toString());
            LogUtils.log("调试", "文件打开处理程序", "读取文件 " + path + "/" + version + ".bf");
        } catch (Exception e) {
            json.key("result").value(-1);
            json.key("errmsg").value(e.getLocalizedMessage());
            LogUtils.logE(e);
        }
        json.endObject();
        byte[] response = json.toString().getBytes();
        httpExchange.sendResponseHeaders(200, response.length);
        OutputStream os = httpExchange.getResponseBody();
        os.write(response);
        os.close();
    }
}
