package elxris.Useless.Commands;

import org.bukkit.command.CommandExecutor;
import org.bukkit.entity.Player;

import elxris.Useless.Utils.Chat;

public abstract class Comando  implements CommandExecutor{
    private Chat chat;
    public Comando(Chat chat) {
        this.chat = chat;
    }
    public void mensaje(Player p, String mensaje, Object...objects){
        chat.mensaje(p, mensaje, objects);
    }
    public void mensaje(String p, String mensaje, Object...objects){
        chat.mensaje(p, mensaje, objects);
    }
    public Chat getChat() {
        return chat;
    }
}
