package elxris.SpiceCraft.Utils;

import java.util.IllegalFormatException;
import java.util.List;

import net.md_5.bungee.chat.ComponentSerializer;

import org.bukkit.entity.Player;

import elxris.SpiceCraft.SpiceCraft;

public class Chat {
    private static void enviar(Player p, String mensaje, Object... i){
        if(mensaje == null || mensaje == ""){
            return;
        }
        if(p == null){
            return;
        }
        try{
        	String m = String.format(mensaje, i);
        	p.sendMessage(m);
        }catch(IllegalFormatException e){
            p.sendMessage(mensaje);
            SpiceCraft.log("MISSING FORMAT ARGUMENT EXCEPTION \n" +
                    "Caused by: "+mensaje);
		}
    }
    
    /* This part is thank to Fanciful
     * 
     * */
    
    private static void enviarJSON(Player p, String mensaje, Object... i){
        if(mensaje == null || mensaje == ""){
            return;
        }
        if(p == null){
            return;
        }
        String m = String.format(mensaje, i);
        p.spigot().sendMessage(ComponentSerializer.parse(m));
    
    }
    public static void mensaje(Player p, List<String> list, Object... i){
        enviar(p, Strings.parseList(list), i);
    }
    public static void mensaje(Player p, String path, Object... i){
        if(path == null){
            return;
        }
        if(Strings.getStringList(path) == null){
        	if (Strings.getJSON(path) != null){
        		enviarJSON(p, Strings.getJSON(path), i);
        		return;
        	}
            enviar(p, path);
            return;
        }
        mensaje(p, Strings.getStringList(path), i);
    }
    public static void mensaje(String p, String m, Object... i){
        Player jugador = SpiceCraft.getOnlinePlayer(p);
        if(jugador == null){
            return;
        }
        mensaje(jugador, m, i);
    }
}
