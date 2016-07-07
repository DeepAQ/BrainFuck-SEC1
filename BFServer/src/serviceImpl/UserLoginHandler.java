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
        LogUtils.log("调试", "用户登录处理程序", "收到新的用户登录请求");
        String username = "";
        String pwdhash = "";
        if (query != null) {
            for (String param : query.split("&")) {
                String pair[] = param.split("=");
                if (pair.length > 1) {
                    if (pair[0].equals("username")) {
                        username = pair[1];
                    } else if (pair[0].equals("pwdhash")) {
                        pwdhash = pair[1];
                    }
                }
            }
        }

        int result = UserMgr.checkLogin(username, pwdhash);
        JSONStringer json = new JSONStringer();
        json.object();
        json.key("result").value(result);
        if (result >= 0) {
            //login success
            String sessionId = UserMgr.newSessionId(username);
            json.key("sessid").value(sessionId);
            LogUtils.log("调试", "用户登录处理程序", username + " 登录成功，会话编码为 " + sessionId);
        } else {
            //login failed
            String errorMsg;
            switch (result) {
                case UserMgr.RESULT_USER_NOEXIST:
                    errorMsg = "用户不存在";
                    break;
                case UserMgr.RESULT_PASSWORD_INCORRECT:
                    errorMsg = "密码不正确";
                    break;
                default:
                    errorMsg = "服务器错误";
                    break;
            }
            json.key("errmsg").value(errorMsg);
            LogUtils.log("调试", "用户登录处理程序", username + " 登录失败：" + errorMsg);
        }
        json.endObject();
        byte[] response = json.toString().getBytes();
        httpExchange.sendResponseHeaders(200, response.length);
        OutputStream os = httpExchange.getResponseBody();
        os.write(response);
        os.close();
    }
}
