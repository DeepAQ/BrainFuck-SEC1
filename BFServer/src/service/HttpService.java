package service;

import com.sun.net.httpserver.HttpServer;
import serviceImpl.*;
import utils.LogUtils;

import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by adn55 on 16/5/12.
 */
public class HttpService {
    private final int port;
    private HttpServer httpServer;

    public HttpService(int port) {
        this.port = port;
    }

    public boolean start() {
        //create thread pool
        ExecutorService executor = Executors.newCachedThreadPool();
        //setup HTTP server
        try {
            httpServer = HttpServer.create(new InetSocketAddress(this.port), 0);
            httpServer.setExecutor(executor);
            httpServer.createContext("/user/login", new UserLoginHandler());
            httpServer.createContext("/user/changepassword", new UserChangePasswordHandler());
            httpServer.createContext("/io/list", new FileListHandler());
            httpServer.createContext("/io/open", new FileOpenHandler());
            httpServer.createContext("/io/save", new FileSaveHandler());
            httpServer.createContext("/io/execute", new ExecuteHandler());
            httpServer.createContext("/io/debug", new DebugHandler());
            httpServer.start();
            LogUtils.log("I", "HttpService", "HTTP service started, listening on port " + this.port);
            return true;
        } catch (Exception e) {
            LogUtils.log("E", e.getClass().getName(), e.getLocalizedMessage());
            LogUtils.log("E", "HttpService", "HTTP service cannot start!");
        }
        return false;
    }

    public void stop() {
        httpServer.stop(0);
        LogUtils.log("I", "HttpService", "HTTP service stopped");
    }
}
