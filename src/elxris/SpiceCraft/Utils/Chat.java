package elxris.SpiceCraft.Utils;

import java.util.List;

import org.bukkit.entity.Player;

import elxris.SpiceCraft.SpiceCraft;

public class Chat {
    private static void enviar(Player p, String mensaje, Object... i){
        if(mensaje == null || mensaje == ""){
            return;
        }
        p.sendMessage(String.format(mensaje, i));
    }
    public static void mensaje(Player p, List<String> list, Object... i){
        if(list == null || list.size() == 0){
            return;
        }
        String texto = "";
        texto = "";
        for(int e = 0; e < list.size(); e++){
            texto += list.get(e);
            if(e + 1 < list.size()){
                texto += "§r\n";
            }
        }
        enviar(p, texto, i);
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
