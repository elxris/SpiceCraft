package elxris.Useless;

import java.util.List;

import org.bukkit.configuration.MemoryConfiguration;
import org.bukkit.configuration.file.FileConfiguration;

import elxris.Useless.Utils.Archivo;

public class CheckConfiguration {
    private boolean changed = false;
    public CheckConfiguration() {
        init();
        // Guarda el archivo de configuración.
        isChanged();
    }
    private void init(){
        FileConfiguration defaults = Useless.getConfig("config.yml");
        if(p().getConfig().getKeys(true).size() == 0){
            Archivo file = new Archivo("config.yml");
            file.save(defaults);
            p().reloadConfig();
        }
        update();
        register(defaults);
        register(Useless.getConfig("lang-"+getString(defaults, "lang")+".yml"));
    }
    private void update() {
        String prev;
        if(!p().getConfig().isSet("v")){
            prev = "0.7.6";
        }else{
            prev = p().getConfig().getStringList("v").get(0);
        }
        if(!prev.contentEquals(Useless.getVersion())){
            delPath("v");
            setPath("v", Useless.getVersion());
        }
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
    private void delPath(String path){
        p().getConfig().set(path, null);
        changed();
    }
    private String getString(MemoryConfiguration mc, String path){
        return mc.getStringList(path).get(0);
    }
    private Useless p(){
        return Useless.plugin();
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
    public boolean changeLang(String s){
        FileConfiguration fc = Useless.getConfig("lang-"+s+".yml");
        if(fc == null){
            return false;
        }
        for(String p: fc.getKeys(false)){
            delPath(p);
        }
        register(fc);
        return true;
    }
}
