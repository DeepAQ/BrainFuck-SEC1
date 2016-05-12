package runner;

import httpSrv.HttpService;

public class ServerRunner {
	public static void main(String[] args) {
        HttpService mainService = new HttpService(8081);
        try {
            mainService.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
