package elxris.Useless.Utils;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
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
    private File getFile(){
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
}
