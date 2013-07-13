package elxris.SpiceCraft.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.configuration.file.FileConfiguration;

public class Strings{
    private static FileConfiguration fc;
    private static List<String> s = new ArrayList<String>();
    public Strings(FileConfiguration fc) {
        setFc(fc);
        s.add("%s");
    }
    private static void setFc(FileConfiguration fc) {
        Strings.fc = fc;
    }
    private static FileConfiguration getFc() {
        return fc;
    }
    public static String parseList(List<String> list){
        if(list == null || list.size() == 0){
            return "";
        }
        String texto = "";
        for(int e = 0; e < list.size(); e++){
            texto += list.get(e);
            if(e + 1 < list.size()){
                texto += "§r\n";
            }
        }
        return texto;
    }
    // GETTERS
    public static String getString(String path){
        return (String) get(path);
    }
    @SuppressWarnings("unchecked")
    public static List<String> getStringList(String path){
        if(path.contentEquals("s")){
            return s;
        }
        return (List<String>) getList(path);
    }
    public static List<String> getStringList(String path, Object... i){
        List<String> list = Arrays.asList(String.format(parseList(getStringList(path)), i).split("§r\\n")); 
        return list;
    }
    public static int getInt(String path){
        return (int) get(path);
    }
    @SuppressWarnings("unchecked")
    public static List<Integer> getIntList(String path){
        return (List<Integer>) getList(path);
    }
    public static double getDouble(String path){
        return (double) get(path);
    }
    @SuppressWarnings("unchecked")
    public static List<Double> getDoubleList(String path){
        return (List<Double>) getList(path);
    }
    public static long getLong(String path){
        return (long) get(path);
    }
    @SuppressWarnings("unchecked")
    public static List<Long> getLongList(String path){
        return (List<Long>) getList(path);
    }
    public static Object get(String path){
        return getList(path).get(0);
    }
    public static List<?> getList(String path){
        return getFc().getList(path);
    }
}
