package elxris.Useless;

import org.bukkit.configuration.file.FileConfiguration;

import elxris.Useless.Utils.Archivo;

public class CheckConfiguration {
    private boolean changed = false;
    public CheckConfiguration() {
        init();
        update();
        // Guarda el archivo de configuración.
        isChanged();
    }
    private void init(){
        FileConfiguration defaults = Useless.getConfig("res/config.yml");
        if(!p().getConfig().isSet("v")){
            Archivo file = new Archivo("config.yml");
            file.save(defaults);
            p().reloadConfig();
        }
        // TODO Procesar los default de distinta manera para iniciarlos si no existen.
        p().getConfig().setDefaults(defaults);
    }
    private void update() {
        String prev;
        if(!p().getConfig().isSet("v")){
            prev = "0.7.6";
        }else{
            prev = p().getConfig().getString("v");
        }
        if(prev.contains("0.7.6") || prev.contains("0.7.7") || prev.contains("0.7.7b")){
            delPath("lib.list");
            delPath("lib.item");
            delPath("lib.itemMe");
            prev = "0.7.7b";
        }
        delPath("v");
        setPath("v", Useless.getVersion());
    }
    private void setPath(String path, Object... v){
        if(!p().getConfig().isSet(path)){
            p().getConfig().set(path, v);
            changed();
        }
    }
    private void delPath(String path){
        p().getConfig().set(path, null);
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
    }
    public void changed() {
        changed = true;
    }
}
