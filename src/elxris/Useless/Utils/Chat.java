package elxris.Useless.Utils;

import java.util.List;

import org.bukkit.entity.Player;

import elxris.Useless.Useless;

public class Chat {
    private static void enviar(Player p, String mensaje, Object... i){
        p.sendMessage(String.format(mensaje, i));
    }
    public static void mensaje(Player p, String path, Object... i){
        String texto = "";
        if(Strings.isList(path)){
            List<String> m = getList(path);
            texto = "";
            for(int e = 0; e < m.size(); e++){
                texto += m.get(e);
                if(e + 1 < m.size()){
                    texto += "\n";
                }
            }
        }else{
            texto = get(path);
        }
        enviar(p, texto, i);
    }
    public static void mensaje(String p, String m, Object... i){
        if(Useless.getPlayer(p) == null){
            return;
        }
        mensaje(Useless.getPlayer(p), m, i);
    }
    private static List<String> getList(String path){
        return Strings.getStringList(path);
    }
    private static String get(String path){
        return Strings.getString(path);
    }
}
