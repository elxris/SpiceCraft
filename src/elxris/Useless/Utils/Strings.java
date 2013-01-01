package elxris.Useless.Utils;

import java.util.List;

import org.bukkit.configuration.file.FileConfiguration;

public class Strings{
    private static FileConfiguration fc;
    public Strings(FileConfiguration fc) {
        setFc(fc);
    }
    private static void setFc(FileConfiguration fc) {
        Strings.fc = fc;
    }
    private static FileConfiguration getFc() {
        return fc;
    }
    // GETTERS
    public static String getString(String path){
        return getFc().getStringList(path).get(0);
    }
    public static List<String> getStringList(String path){
        return getFc().getStringList(path);
    }
    public static int getInt(String path){
        return getFc().getIntegerList(path).get(0);
    }
    public static List<Integer> getIntList(String path){
        return getFc().getIntegerList(path);
    }
    public static double getDouble(String path){
        return getFc().getDoubleList(path).get(0);
    }
    public static List<Double> getDoubleList(String path){
        return getFc().getDoubleList(path);
    }
    public static long getLong(String path){
        return getFc().getLongList(path).get(0);
    }
    public static List<Long> getLongList(String path){
        return getFc().getLongList(path);
    }
    public static Object get(String path){
        return getFc().getList(path).get(0);
    }
    public static Object getList(String path){
        return getFc().getList(path);
    }
}
