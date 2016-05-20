package serviceImpl;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.json.JSONStringer;
import utils.LogUtils;
import utils.UserMgr;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by adn55 on 16/5/19.
 */
public class UserLoginHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String query = httpExchange.getRequestURI().getQuery();
        LogUtils.log("D", "UserLoginHandler", "New /user/login request with query " + query);
        String username = "";
        String password = "";
        if (query != null) {
            for (String param : query.split("&")) {
                String pair[] = param.split("=");
                if (pair.length > 1) {
                    if (pair[0].equals("username")) {
                        username = pair[1];
                    } else if (pair[0].equals("password")) {
                        password = pair[1];
                    }
                }
            }
        }
        int result = UserMgr.checkLogin(username, password);
        System.out.println(result);
        JSONStringer json = new JSONStringer();
        json.object();
        json.key("result").value(result);
        if (result >= 0) {
            //login success
            json.key("sessionid").value(UserMgr.newSessionId(username));
        } else {
            //login failed
            String errorMsg;
            switch (result) {
                case UserMgr.RESULT_USER_NOEXIST:
                    errorMsg = "user_no_exist";
                    break;
                case UserMgr.RESULT_PASSWORD_INCORRECT:
                    errorMsg = "password_incorrect";
                    break;
                default:
                    errorMsg = "system_error";
                    break;
            }
            json.key("error_msg").value(errorMsg);
        }
        json.endObject();
        String response = json.toString();
        httpExchange.sendResponseHeaders(200, response.length());
        httpExchange.getResponseHeaders().add("Content-type", "application/json");
        OutputStream os = httpExchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
}
