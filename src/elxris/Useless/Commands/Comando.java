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
}
