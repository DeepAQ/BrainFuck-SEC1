package serviceImpl;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.json.JSONStringer;
import utils.LogUtils;
import utils.UserMgr;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by adn55 on 16/5/25.
 */
public class FileListHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String query = httpExchange.getRequestURI().getQuery();
        LogUtils.log("调试", "文件列表处理程序", "收到新的文件列表请求");
        String sessid = "";
        if (query != null) {
            for (String param : query.split("&")) {
                String pair[] = param.split("=");
                if (pair.length > 1 && pair[0].equals("sessid")) {
                    sessid = pair[1];
                }
            }
        }
        String username = UserMgr.getUsernameBySessionId(sessid);
        if (username == null) {
            httpExchange.sendResponseHeaders(403, 0);
            httpExchange.close();
            return;
        }

        JSONStringer json = new JSONStringer();
        json.array();
        String path = "objects/" + username;
        File tmpFile = new File(path);
        if (tmpFile.isDirectory()) {
            for (String file : tmpFile.list()) {
                File tmpFile2 = new File(path + "/" + file);
                if (tmpFile2.isDirectory()) {
                    json.object();
                    json.key("filename").value(file);
                    json.key("versions").array();
                    for (String version : tmpFile2.list()) {
                        json.value(version.replace(".bf", ""));
                    }
                    json.endArray();
                    json.endObject();
                }
            }
        }
        json.endArray();
        byte[] response = json.toString().getBytes();
        httpExchange.sendResponseHeaders(200, response.length);
        OutputStream os = httpExchange.getResponseBody();
        os.write(response);
        os.close();
    }
}
