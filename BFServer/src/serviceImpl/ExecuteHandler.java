package serviceImpl;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import utils.LogUtils;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by adn55 on 16/5/12.
 */
public class ExecuteHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String query = httpExchange.getRequestURI().getQuery();
        LogUtils.log("D", "ExecuteHandler", "New /io/execute request with query " + query);
        String code = "";
        String input = "";
        if (query != null) {
            for (String param : query.split("&")) {
                String pair[] = param.split("=");
                if (pair.length > 1) {
                    if (pair[0].equals("code")) {
                        code = pair[1];
                    } else if (pair[0].equals("input")) {
                        input = pair[1];
                    }
                }
            }
        }
        String response = "error";
        if (!code.isEmpty()) {
            response = new ExecuteServiceImpl().execute(code, input);
        }
        LogUtils.log("D", "ExecuteHandler", "Respond with result \"" + response + "\"");
        httpExchange.sendResponseHeaders(200, response.length());
        httpExchange.getResponseHeaders().add("Content-type", "text/plain");
        OutputStream os = httpExchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
}
