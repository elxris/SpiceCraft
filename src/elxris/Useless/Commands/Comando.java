package elxris.Useless.Commands;

import java.util.List;

import org.bukkit.command.CommandExecutor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import elxris.Useless.Useless;
import elxris.Useless.Utils.Chat;

public abstract class Comando  implements CommandExecutor{
    public void mensaje(Player p, String mensaje, Object...objects){
        Chat.mensaje(p, mensaje, objects);
    }
    public void mensaje(String p, String mensaje, Object...objects){
        Chat.mensaje(p, mensaje, objects);
    }
    public void mensaje(Player p, List<String> mensaje, Object...objects){
        Chat.mensaje(p, mensaje, objects);
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
        return Useless.plugin().getConfig();
    }
    public boolean isCommand(String path, String command){
        for(String s: getConfig().getStringList(path)){
            if(s.contentEquals(command)){
                return true;
            }
        }
        return false;
    }
    public Object getValue(String path){
        return getConfig().get(path);
    }
}
