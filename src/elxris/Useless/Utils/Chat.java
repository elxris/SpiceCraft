package elxris.Useless.Utils;

import org.bukkit.Server;
import org.bukkit.entity.Player;

public class Chat {
    Server s;
    
    public Chat(Server s){
        this.s = s;
    }
    
    public void mensaje(Player p, String m, Object... i){
        s.getPlayer(p.getName()).sendMessage(String.format(m, i));
    }
    public void mensaje(String p, String m, Object... i){
        s.getPlayer(p).sendMessage(String.format(m, i));
    }
}
