package elxris.Useless.Commands;

import org.bukkit.command.CommandExecutor;
import org.bukkit.entity.Player;

import elxris.Useless.Utils.Chat;
import elxris.Useless.Utils.Strings;

public abstract class Comando  implements CommandExecutor{
    public void mensaje(Player p, String mensaje, Object...objects){
        Chat.mensaje(p, mensaje, objects);
    }
    public void mensaje(String p, String mensaje, Object...objects){
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
    public boolean isCommand(String path, String command){
        for(String s: Strings.getStringList(path)){
            if(s.contentEquals(command)){
                return true;
            }
        }
        return false;
    }
}
