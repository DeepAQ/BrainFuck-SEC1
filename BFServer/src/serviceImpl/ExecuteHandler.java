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
        LogUtils.log("调试", "运行处理程序", "接收到新的运行请求");
        String sessid = "";
        String code = "";
        String input = "";
        if (query != null) {
            for (String param : query.split("&")) {
                String pair[] = param.split("=");
                if (pair.length > 1) {
                    switch (pair[0]) {
                        case "code":
                            code = URLDecoder.decode(pair[1], "utf-8");
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
        if (UserMgr.getUsernameBySessionId(sessid) == null) {
            httpExchange.sendResponseHeaders(403, 0);
            httpExchange.close();
            return;
        }

        JSONStringer json = new JSONStringer();
        json.object();
        long usedTime = System.currentTimeMillis();
        ExecuteServiceImpl executeService = new ExecuteServiceImpl();
        String output = executeService.execute(code, input);
        usedTime = System.currentTimeMillis() - usedTime;
        json.key("result").value(0);
        json.key("output").value(output);
        json.key("time").value(Long.toString(usedTime));
        json.endObject();
        LogUtils.log("调试", "运行处理程序", "运行成功，用时 " + usedTime + " 毫秒");
        byte[] response = json.toString().getBytes();
        httpExchange.sendResponseHeaders(200, response.length);
        OutputStream os = httpExchange.getResponseBody();
        os.write(response);
        os.close();
    }
}
