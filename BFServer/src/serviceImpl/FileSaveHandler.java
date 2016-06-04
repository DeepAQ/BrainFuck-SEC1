package serviceImpl;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.json.JSONStringer;
import utils.LogUtils;
import utils.UserMgr;

import java.io.*;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * Created by adn55 on 16/5/24.
 */
public class FileSaveHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String query = httpExchange.getRequestURI().getQuery();
        LogUtils.log("D", getClass().getSimpleName(), "New /io/save request with query " + query);
        String sessid = "";
        String code = "";
        String filename = "";
        if (query != null) {
            for (String param : query.split("&")) {
                String pair[] = param.split("=");
                if (pair.length > 1) {
                    switch (pair[0]) {
                        case "code":
                            code = URLDecoder.decode(pair[1], "utf-8");
                            break;
                        case "filename":
                            filename = pair[1];
                            break;
                        case "sessid":
                            sessid = pair[1];
                            break;
                    }
                }
            }
        }
        String username = UserMgr.getUsernameBySessionId(sessid);
        if (username == null) return;
        if (filename == null) return;

        JSONStringer json = new JSONStringer();
        json.object();
        try {
            String version = new SimpleDateFormat("yyMMdd-HHmmss").format(new Date());
            String path = "objects/" + username + "/" + filename;
            File tmpFile = new File(path);
            if (!tmpFile.isDirectory()) {
                tmpFile.delete();
                tmpFile.mkdirs();
            }
            OutputStreamWriter writer = new OutputStreamWriter(
                    new FileOutputStream(path + "/" + version + ".bf"),
                    "utf-8"
            );
            writer.write(code);
            writer.flush();
            writer.close();
            json.key("result").value(0);
            json.key("version").value(version);
            LogUtils.log("D", getClass().getSimpleName(), "Wrote to file " + path + "/" + version + ".bf");
        } catch (Exception e) {
            json.key("result").value(-1);
            json.key("errmsg").value(e.getLocalizedMessage());
            LogUtils.logE(e);
        }
        json.endObject();
        String response = json.toString();
        httpExchange.sendResponseHeaders(200, response.length());
        OutputStream os = httpExchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
}
