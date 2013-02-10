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
        if(!exist()){
            loadResourse(getName());
        }
        return YamlConfiguration.loadConfiguration(getFile());
    }
    public void save(FileConfiguration fc){
        try {
            fc.save(getFile());
        } catch (IOException e) {
            Useless.log(Strings.getString("alert.notsaved")+getName());
        }
    }
    private void saveString(String data){
        try {
            FileWriter fw = new FileWriter(getFile());
            fw.write(data);
            fw.close();
        } catch (IOException e) {}
    }
    public void loadResourse(String path){ // Carga un recurso del jar y lo guarda en el archivo.
        InputStream is = Useless.plugin().getResource("res/"+path);
        if(is == null){
            return;
        }
        Scanner s = new Scanner(is, Charsets.ISO_8859_1.displayName()).useDelimiter("\\A");
        saveString(s.next());
    }
    public static FileConfiguration getDefaultConfig(String path){ // Carga una configuración.
        FileConfiguration fc = new YamlConfiguration();
        Archivo file = new Archivo("tmp."+path);
        
        try {fc.save(file.getFile());} catch (IOException e) {e.printStackTrace();}
        
        file.loadResourse(path); // Carga el recurso.
        fc = file.load();
        file.getFile().delete();
        return fc;
    }
}
