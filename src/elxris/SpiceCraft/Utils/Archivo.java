package elxris.SpiceCraft.Utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.google.common.base.Charsets;

import elxris.SpiceCraft.SpiceCraft;
import elxris.SpiceCraft.Objects.Savable;

public class Archivo extends Savable{
    private File file;
    private String name;
    private FileConfiguration data;
    public Archivo(String nombre){
        setName(nombre);
        setFile();
    }
    public String getName(){
        return name;
    }
    private void setName(String nombre){
        name = nombre;
    }
    public File getFile(){
        return file;
    }
    public void setFile(){
        file = new File(SpiceCraft.plugin().getDataFolder(), getName());
    }
    public boolean exist(){
        return getFile().exists();
    }
    public FileConfiguration load(){
        if(!exist()){
            loadResourse(getName());
            if(!exist()){
                blankFile(getName());
            }
        }
        return YamlConfiguration.loadConfiguration(getFile());
    }
    public void saveNow(FileConfiguration fc){
        try {
            fc.save(getFile());
        } catch (IOException e) {
            SpiceCraft.log(Strings.getString("alert.notsaved")+getName());
        }
    }
    public void saveNow(Configuration fc){
        saveNow((FileConfiguration) fc);
    }
    public void saveString(String data){
        try {
            FileWriter fw = new FileWriter(getFile(), true);
            fw.append(data);
            fw.close();
        } catch (IOException e) {
        }
    }
    public boolean loadResourse(String path){ // Carga un recurso del jar y lo guarda en el archivo.
        InputStream is = SpiceCraft.plugin().getResource("res/"+path);
        if(is == null){
            return false;
        }
        Scanner s = new Scanner(is, Charsets.UTF_8.displayName()).useDelimiter("\\A");
        if(!exist()){
            blankFile(getName());
        }
        saveString(s.next());
        return true;
    }
    public static void blankFile(String name){
        FileConfiguration fc = new YamlConfiguration();
        Archivo file = new Archivo(name);
        
        // Crea el archivo y carpetas necesarias.
        try {fc.save(file.getFile());} catch (IOException e) {e.printStackTrace();}
    }
    public static FileConfiguration getDefaultConfig(String path){ // Carga una configuración.
        FileConfiguration fc = new YamlConfiguration();
        Archivo file = new Archivo("tmp."+path);
        blankFile("tmp."+path);
        file.loadResourse(path); // Carga el recurso.
        fc = file.load();
        file.getFile().delete();
        return fc;
    }
    public void setData(FileConfiguration data){
        this.data = data;
    }
    public void save(FileConfiguration data){
        setData(data);
        save();
    }
    public void save(Configuration data){
        save((FileConfiguration)data);
    }
    @Override
    public void run(){
        super.run();
        saveNow(this.data);
    }
}
