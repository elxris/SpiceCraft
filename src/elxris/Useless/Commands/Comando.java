package elxris.Useless.Commands;

import org.bukkit.command.CommandExecutor;
import org.bukkit.entity.Player;

import elxris.Useless.Utils.Chat;

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
}
