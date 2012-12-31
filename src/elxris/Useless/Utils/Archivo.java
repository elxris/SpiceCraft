package elxris.Useless.Utils;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

public class Archivo {
    private File file;
    private String name;
    private Plugin p;
    
    public Archivo(Plugin p, String nombre){
        setPlugin(p);
        setFile(nombre);
    }
    public String getName(){
        return name;
    }
    public void setName(String nombre){
        name = nombre;
    }
    public File getFile(){
        return file;
    }
    public void setFile(String name){
        setName(name);
        file = new File(getPlugin().getDataFolder(), getName());
    }
    public Plugin getPlugin(){
        return p;
    }
    public void setPlugin(Plugin pl){
        p = pl;
    }
    public FileConfiguration load(){
        return YamlConfiguration.loadConfiguration(getFile());
    }
    public void save(FileConfiguration fc){
        try {
            fc.save(getFile());
        } catch (IOException e) {
            getPlugin().getLogger().info(getPlugin().getConfig().getString("alert.notsaved")+getName());
        }
    }
}
