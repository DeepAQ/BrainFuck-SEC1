package serviceImpl;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.json.JSONStringer;
import utils.DebugMgr;
import utils.LogUtils;
import utils.UserMgr;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLDecoder;

/**
 * Created by adn55 on 16/7/1.
 */
public class DebugHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String query = httpExchange.getRequestURI().getQuery();
        String sessid = "";
        String action = "";
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
                        case "action":
                            action = pair[1];
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

        if (action.equals("stop")) {
            DebugMgr.debugSessions.remove(sessid);
            System.gc();
            DebugMgr.debugSessions.put(sessid, new ExecuteServiceImpl(code, input));
            json.key("result").value(0);
        } else {
            if (action.equals("start")) {
                DebugMgr.debugSessions.remove(sessid);
                DebugMgr.debugSessions.put(sessid, new ExecuteServiceImpl(code, input));
                LogUtils.log("D", getClass().getSimpleName(), "Started new debug session with query " + query);
            }
            try {
                if (!DebugMgr.debugSessions.containsKey(sessid)) {
                    throw new Exception("Debug session does not exist!");
                }
                ExecuteServiceImpl session = DebugMgr.debugSessions.get(sessid);
                if (action.equals("step")) {
                    session.step();
                } else if (action.equals("resume")) {
                    session.resume();
                }
                json.key("result").value(0);
                json.key("memptr").value(session.memoryPtr);
                json.key("codeptr").value(session.codePtr);
                json.key("inputptr").value(session.inputPtr);
                json.key("output").value(session.output);
                json.key("memory").array();
                for (byte b : session.memory) {
                    json.value(b);
                }
                json.endArray();
            } catch (Exception e) {
                json.key("result").value(-1);
                json.key("errmsg").value(e.getLocalizedMessage());
                DebugMgr.debugSessions.remove(sessid);
            }
        }

        json.endObject();
        byte[] response = json.toString().getBytes();
        httpExchange.sendResponseHeaders(200, response.length);
        OutputStream os = httpExchange.getResponseBody();
        os.write(response);
        os.close();
    }
}
