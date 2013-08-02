package elxris.SpiceCraft.Commands;

import java.util.List;

import org.bukkit.command.CommandExecutor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import elxris.SpiceCraft.SpiceCraft;
import elxris.SpiceCraft.Utils.Chat;
import elxris.SpiceCraft.Utils.Econ;

public abstract class Comando implements CommandExecutor{
    private static Econ econ;
    public void mensaje(Player p, String path, Object...objects){
        Chat.mensaje(p, path, objects);
    }
    public void mensaje(String p, String mensaje, Object...objects){
        Chat.mensaje(p, mensaje, objects);
    }
    public void mensaje(Player p, List<String> mensaje, Object...objects){
        Chat.mensaje(p, mensaje, objects);
    }
    public Player getPlayer(String p){
        return SpiceCraft.getOnlinePlayer(p);
    }
    public boolean isInteger(String s){
        try {
            Integer.parseInt(s);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    public boolean isDouble(String s){
        try {
            Double.parseDouble(s);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    public FileConfiguration getConfig(){
        return SpiceCraft.plugin().getConfig();
    }
    public boolean isCommand(String path, String command){
        if(!getConfig().isSet(path)){
            return false;
        }
        for(String s: getConfig().getStringList(path)){
            if(s.toLowerCase().contentEquals(command.toLowerCase())){
                return true;
            }
        }
        return false;
    }
    public Object getValue(String path){
        return getConfig().get(path);
    }
    public void setEcon(Econ econ) {
        Comando.econ = econ;
    }
    public Econ getEcon() {
        if(econ == null){
            setEcon(new Econ());
        }
        return econ;
    }
}
