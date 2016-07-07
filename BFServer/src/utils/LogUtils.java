package utils;

import javafx.scene.control.TextArea;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

/**
 * Created by adn55 on 16/5/14.
 */
public class LogUtils {
    private static TextArea logArea;
    private static final String[] logLevels = new String[]{"调试", "信息", "警告", "错误"};
    private static int logLevel = 0;

    public static void init(TextArea textArea) {
        logArea = textArea;
    }

    public static void setLogLevel(int level) {
        logLevel = level;
    }

    public static void log(String type, String tag, String logs) {
        if (Arrays.asList(logLevels).indexOf(type) >= logLevel) {
            String logText = "";
            logText += new SimpleDateFormat("[yy/MM/dd HH:mm:ss.SSS] ").format(new Date());
            logText += type + "/" + tag + ": " + logs + System.getProperty("line.separator");
            logArea.appendText(logText);
        }
    }

    public static void logE(Exception e) {
        log("E", e.getClass().getSimpleName(), e.getLocalizedMessage());
        e.printStackTrace();
    }
}
