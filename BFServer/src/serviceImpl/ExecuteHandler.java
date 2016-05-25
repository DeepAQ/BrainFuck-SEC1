package serviceImpl;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.json.JSONStringer;
import utils.LogUtils;
import utils.UserMgr;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLDecoder;

/**
 * Created by adn55 on 16/5/12.
 */
public class ExecuteHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String query = httpExchange.getRequestURI().getQuery();
        LogUtils.log("D", "ExecuteHandler", "New /io/execute request with query " + query);
        String sessid = "";
        String code = "";
        String input = "";
        if (query != null) {
            for (String param : query.split("&")) {
                String pair[] = param.split("=");
                if (pair.length > 1) {
                    switch (pair[0]) {
                        case "code":
                            code = pair[1];
                            break;
                        case "input":
                            input = URLDecoder.decode(pair[1], "utf-8");
                            break;
                        case "sessid":
                            sessid = pair[1];
                            break;
                    }
                }
            }
        }
        if (UserMgr.getUsernameBySessionId(sessid) == null) return;

        JSONStringer json = new JSONStringer();
        json.object();
        ExecuteServiceImpl executeService = new ExecuteServiceImpl();
        String output = executeService.execute(code, input);
        if (output == null) {
            json.key("result").value(-1);
            json.key("errmsg").value(executeService.errorMessage);
        } else {
            json.key("result").value(0);
            json.key("output").value(output);
        }
        json.endObject();
        String response = json.toString();
        LogUtils.log("D", "ExecuteHandler", "Execute result is \"" + output + "\"");
        httpExchange.sendResponseHeaders(200, response.length());
        OutputStream os = httpExchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
}
