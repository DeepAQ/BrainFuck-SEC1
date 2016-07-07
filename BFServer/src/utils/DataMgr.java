package utils;

import java.io.*;
import java.util.HashMap;

/**
 * Created by adn55 on 16/5/19.
 */
public class DataMgr {
    public static Data data = new Data();
    private static final String fileName = "操脑服务器.数据";

    public static void loadFromFile() {
        try {
            ObjectInputStream is = new ObjectInputStream(new FileInputStream(fileName));
            data = (Data) is.readObject();
            is.close();
        } catch (Exception e) {
            LogUtils.log("警告", "数据管理器", "读取数据文件失败，尝试创建新文件。");
            UserMgr.addUser("demo", "demo");
            saveToFile();
        }
    }

    public static void saveToFile() {
        try {
            ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(fileName));
            os.writeObject(data);
            os.close();
        } catch (Exception e) {
            LogUtils.logE(e);
            LogUtils.log("错误", "数据管理器", "保存数据文件失败！");
        }
    }
}

class Data implements Serializable {
    public final HashMap<String, String> users = new HashMap<>();
}