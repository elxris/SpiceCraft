package elxris.Useless.Utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.google.common.base.Charsets;

import elxris.Useless.Useless;

public class Archivo {
    private File file;
    private String name;
    
    public Archivo(String nombre){
        setName(nombre);
        setFile();
    }
    private String getName(){
        return name;
    }
    private void setName(String nombre){
        name = nombre;
    }
    public File getFile(){
        return file;
    }
    public void setFile(){
        file = new File(Useless.plugin().getDataFolder(), getName());
    }
    public boolean exist(){
        return getFile().exists();
    }
    public FileConfiguration load(){
        return YamlConfiguration.loadConfiguration(getFile());
    }
    public void save(FileConfiguration fc){
        try {
            fc.save(getFile());
        } catch (IOException e) {
            Useless.log(Strings.getString("alert.notsaved")+getName());
        }
    }
    public void saveString(String data){
        try {
            FileWriter fw = new FileWriter(getFile());
            fw.write(data);
            fw.close();
        } catch (IOException e) {}
    }
    public void loadResourse(String path){
        InputStream is = Useless.plugin().getResource("res/"+path);
        if(is == null){
            return;
        }
        Scanner s = new Scanner(is, Charsets.ISO_8859_1.displayName()).useDelimiter("\\A");
        saveString(s.next());
    }
}
