package elxris.SpiceCraft.Utils;

import java.util.List;
import java.util.MissingFormatArgumentException;

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
        String m;
        try{
            m = String.format(mensaje, i);
            p.sendMessage(m);
        }catch(MissingFormatArgumentException ex){
            p.sendMessage(mensaje);
            SpiceCraft.log("MISSING FORMAT ARGUMENT EXCEPTION \n" +
                    "Caused by: "+mensaje);
        }
    }
    public static void mensaje(Player p, List<String> list, Object... i){
        enviar(p, Strings.parseList(list), i);
    }
    public static void mensaje(Player p, String path, Object... i){
        mensaje(p, Strings.getStringList(path), i);
    }
    public static void mensaje(String p, String m, Object... i){
        if(SpiceCraft.getPlayer(p) == null){
            return;
        }
        Player jugador = SpiceCraft.getPlayer(p);
        if(Strings.getStringList(m) == null){
            enviar(jugador, m);
            return;
        }
        mensaje(jugador, m, i);
    }
}
