package utils;

import java.io.*;
import java.util.HashMap;

/**
 * Created by adn55 on 16/5/19.
 */
public class DataMgr {
    public static Data data = new Data();
    private static final String fileName = "BFServer.dat";

    public static void loadFromFile() {
        try {
            ObjectInputStream is = new ObjectInputStream(new FileInputStream(fileName));
            data = (Data) is.readObject();
            is.close();
        } catch (Exception e) {
            LogUtils.log("W", "DataMgr", "Cannot read data from file, trying to create a new one.");
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
            LogUtils.log("E", "DataMgr", "Save data to file failed!");
        }
    }
}

class Data implements Serializable {
    public final HashMap<String, String> users = new HashMap<>();
}