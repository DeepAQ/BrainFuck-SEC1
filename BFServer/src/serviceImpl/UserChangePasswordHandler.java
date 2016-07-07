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
        LogUtils.log("调试", "用户密码修改处理程序", "收到新的密码修改请求");
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
        if (username == null) {
            httpExchange.sendResponseHeaders(403, 0);
            httpExchange.close();
            return;
        }

        int result = UserMgr.changePassword(username, oldPwdhash, newPwdhash);
        JSONStringer json = new JSONStringer();
        json.object();
        json.key("result").value(result);
        if (result < 0) {
            // failed
            String errorMsg;
            switch (result) {
                case UserMgr.RESULT_USER_NOEXIST:
                    errorMsg = "用户不存在";
                    break;
                case UserMgr.RESULT_PASSWORD_INCORRECT:
                    errorMsg = "旧密码不正确";
                    break;
                default:
                    errorMsg = "服务器错误";
                    break;
            }
            json.key("errmsg").value(errorMsg);
            LogUtils.log("调试", "用户密码修改处理程序", username + " 修改密码失败：" + errorMsg);
        } else {
            LogUtils.log("调试", "用户密码修改处理程序", username + " 修改密码成功");
        }
        json.endObject();
        byte[] response = json.toString().getBytes();
        httpExchange.sendResponseHeaders(200, response.length);
        OutputStream os = httpExchange.getResponseBody();
        os.write(response);
        os.close();
    }
}
