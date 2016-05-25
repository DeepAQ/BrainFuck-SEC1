package utils;

import java.io.*;

/**
 * Created by adn55 on 16/5/21.
 */
public class DataMgr {
    public static BFData data = new BFData();
    private static String fileName = "BFClient.dat";

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
            System.out.println("Save data to file failed!");
        }
    }
}