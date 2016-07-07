package utils;

import java.io.*;

/**
 * Created by adn55 on 16/5/21.
 */
public class DataMgr {
    public static BFData data = new BFData();
    private static final String fileName = "操脑客户端.数据";

    public static void loadFromFile() {
        try {
            ObjectInputStream is = new ObjectInputStream(new FileInputStream(fileName));
            data = (BFData) is.readObject();
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void saveToFile() {
        try {
            ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(fileName));
            os.writeObject(data);
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("保存数据文件失败！");
        }
    }
}