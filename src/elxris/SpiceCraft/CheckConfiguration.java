package elxris.SpiceCraft;

import java.util.List;

import org.bukkit.configuration.file.FileConfiguration;

import elxris.SpiceCraft.Utils.Archivo;
import elxris.SpiceCraft.Utils.Strings;

public class CheckConfiguration {
    private boolean changed = false;
    public CheckConfiguration() {
        init();
        // Guarda el archivo de configuración.
        isChanged();
    }
    private void init(){
        FileConfiguration defaults = Archivo.getDefaultConfig("config.yml");
        if(p().getConfig().getKeys(true).size() == 0){
            Archivo file = new Archivo("config.yml");
            file.save(defaults);
            p().reloadConfig();
        }
        register(defaults);
        Archivo lang = new Archivo("lang-"+p().getConfig().getString("lang")+".yml");
        if(!lang.exist()){
            if(!lang.loadResourse("lang-"+p().getConfig().getString("lang")+".yml")){
                lang = new Archivo("lang-"+defaults.getString("lang")+".yml");
                lang.loadResourse("lang-"+defaults.getString("lang")+".yml");
            }
        }
        new Strings(lang.load());
    }
    // Registra las configuraciones que no existan en el config.yml del cliente.
    private void register(FileConfiguration fc){
        if(fc == null){
            return;
        }
        for(String s: fc.getKeys(true)){
            setDef(s, fc.get(s));
        }
    }
    private void setPath(String path, Object... v){
        if(!p().getConfig().isSet(path)){
            p().getConfig().set(path, v);
            changed();
        }
    }
    private void setDef(String path, Object v){
        if(v instanceof List){
            setPath(path, ((List<?>) v).toArray());
        }else{
            setPath(path, v);
        }
    }
    private SpiceCraft p(){
        return SpiceCraft.plugin();
    }
    private void isChanged(){
        if(!changed){
            return;
        }
        p().saveConfig();
        p().reloadConfig();
        changed = false;
    }
    public void changed() {
        changed = true;
    }
}
