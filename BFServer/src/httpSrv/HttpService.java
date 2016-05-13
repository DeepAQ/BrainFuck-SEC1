package httpSrv;

import com.sun.net.httpserver.HttpServer;
import ui.LogUtils;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by adn55 on 16/5/12.
 */
public class HttpService {
    private int port;
    private ExecutorService executor;
    private HttpServer httpServer;

    public HttpService(int port) {
        this.port = port;
    }

    public void start() throws IOException {
        //create thread pool
        executor = Executors.newCachedThreadPool();
        //setup HTTP server
        httpServer = HttpServer.create(new InetSocketAddress(this.port), 0);
        httpServer.setExecutor(executor);
        httpServer.createContext("/io/execute", new ExecuteHandler());
        httpServer.start();
        LogUtils.log("I", "HttpService", "HTTP service started, listening on port " + this.port);
    }

    public void stop() {
        httpServer.stop(0);
        LogUtils.log("I", "HttpService", "HTTP service stopped");
    }
}
