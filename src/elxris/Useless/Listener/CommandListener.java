package elxris.Useless.Listener;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.Listener;

import elxris.Useless.Utils.Archivo;
import elxris.Useless.Utils.Chat;

public class CommandListener implements Listener{
    private Archivo file;
    private FileConfiguration fc;
    public CommandListener() {
        load("commands.yml");
        init();
    }
    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent event){
        if(getCommand(event.getPlayer(), event.getMessage())){
            event.setCancelled(true);
        }
    }
    // Inicia y prepara la configuración.
    private void init(){
        if(!getFc().isSet("commands") || !getFc().isList("commands")){
            String[] ejemplo = {"comando1", "comando2"};
            setPath("commands", ejemplo);
            save();
        }
        for(String s: getFc().getStringList("commands")){
            if(!getFc().isSet(s)){
                setPath("root."+s, "Mensaje sin argumentos.");
                setPath(s+".1", "Mensaje con argumento '1'.");
                setPath(s+".dos", "Mensaje con argumento 'dos'.");
                setPath(s+".pastel.moras", "Mensaje con argumentos 'pastel moras'");
                save();
            }
        }
    }
    // Muestra un comando
    private boolean getCommand(Player p, String command){
        String[] s = command.split(" ");
        command = command.replace(' ', '.');
        command = command.substring(1);
        if(s.length == 1){
            return mensaje(p, "root."+command);
        }
        if(s.length > 1){
            return mensaje(p, command);
        }
        return false;
    }
    private boolean mensaje(Player p, String path) {
        if(getFc().isSet(path) && getFc().isList(path)){
            Chat.mensaje(p, getFc().getStringList(path));
            return true;
        }
        return false;
    }
    
    // SetPath, Cargar y Guardar.
    private void setPath(String path, Object... o){
        getFc().set(path, o);
    }
    private void save(FileConfiguration fc){
        getFile().save(fc);
    }
    private void save(){
        save(fc);
        reload();
    }
    private void load(String name){
        setFile(new Archivo(name));
        setFc(getFile().load());
    }
    private void reload(){
        setFc(getFile().load());
    }
    //Getters y Setters
    private Archivo getFile() {
        return file;
    }
    private void setFile(Archivo file) {
        this.file = file;
    }
    private FileConfiguration getFc() {
        return fc;
    }
    private void setFc(FileConfiguration fc) {
        this.fc = fc;
    }
}
