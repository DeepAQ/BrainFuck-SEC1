package ui;

import javafx.scene.control.TextArea;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by adn55 on 16/5/14.
 */
public class LogUtils {
    public static TextArea logArea;

    public static void log(String type, String tag, String logs) {
        String logText = "";
        logText += new SimpleDateFormat("[yy/MM/dd HH:mm:ss.SSS] ").format(new Date());
        logText += type + "/" + tag + ": " + logs + System.getProperty("line.separator");
        logArea.appendText(logText);
    }
}
