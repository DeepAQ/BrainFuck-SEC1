package serviceImpl;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.json.JSONStringer;
import utils.LogUtils;
import utils.UserMgr;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by adn55 on 16/6/20.
 */
public class UserChangePasswordHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String query = httpExchange.getRequestURI().getQuery();
        LogUtils.log("D", getClass().getSimpleName(), "New /user/changepassword request with query " + query);
        String sessid = "";
        String oldPwdhash = "";
        String newPwdhash = "";
        if (query != null) {
            for (String param : query.split("&")) {
                String pair[] = param.split("=");
                if (pair.length > 1) {
                    switch (pair[0]) {
                        case "sessid":
                            sessid = pair[1];
                            break;
                        case "oldpwdhash":
                            oldPwdhash = pair[1];
                            break;
                        case "newpwdhash":
                            newPwdhash = pair[1];
                            break;
                    }
                }
            }
        }
        String username = UserMgr.getUsernameBySessionId(sessid);
        if (username == null) return;
        int result = UserMgr.changePassword(username, oldPwdhash, newPwdhash);
        JSONStringer json = new JSONStringer();
        json.object();
        json.key("result").value(result);
        if (result < 0) {
            // failed
            String errorMsg;
            switch (result) {
                case UserMgr.RESULT_USER_NOEXIST:
                    errorMsg = "User does not exist";
                    break;
                case UserMgr.RESULT_PASSWORD_INCORRECT:
                    errorMsg = "Old password incorrect";
                    break;
                default:
                    errorMsg = "Server error";
                    break;
            }
            json.key("errmsg").value(errorMsg);
            LogUtils.log("D", getClass().getSimpleName(), username + " change password failed: " + errorMsg);
        } else {
            LogUtils.log("D", getClass().getSimpleName(), username + " change password succeeded");
        }
        json.endObject();
        String response = json.toString();
        httpExchange.sendResponseHeaders(200, response.length());
        OutputStream os = httpExchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
}
