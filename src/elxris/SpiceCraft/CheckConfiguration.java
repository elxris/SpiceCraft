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
            file.loadResourse("config.yml");
            p().reloadConfig();
        }
        register(defaults);
        String langCode = p().getConfig().getString("lang");
        Archivo lang = new Archivo("lang-"+langCode+".yml");
        // Si no existe el archivo de idioma.
        if(!lang.exist()){
            // Si no existe el recurso en el Plugin
            if(!lang.loadResourse("lang-"+langCode+".yml")){
                lang = new Archivo("lang-"+langCode+".yml");
                langCode = defaults.getString("lang");
                lang.loadResourse("lang-"+langCode+".yml");
            }
        }
        // Carga la configuración del usuario
        FileConfiguration langConfig = lang.load();
        // Carga la configuracion por defecto.
        Archivo langTmp = new Archivo("lang.yml.tmp");
        langTmp.loadResourse("lang-"+langCode+".yml");
        // Establece por defecto la configuración.
        langConfig.setDefaults(langTmp.load());
        // Borra la configuración por defecto temporal.
        langTmp.getFile().delete();
        new Strings(langConfig);
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
