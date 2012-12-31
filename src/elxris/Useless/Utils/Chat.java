package elxris.Useless.Utils;

import java.util.List;

import org.bukkit.entity.Player;

import elxris.Useless.Useless;

public class Chat {
    Useless plugin;
    
    public Chat(Useless plugin){
        this.plugin = plugin;
    }
    private void enviar(Player p, String mensaje, Object... i){
        //plugin.log(mensaje);
        //plugin.log(p.getDisplayName());
        p.sendMessage(String.format(mensaje, i));
    }
    public void mensaje(Player p, String path, Object... i){
        String texto = "";
        if(plugin.getConfig().isList(path)){
            List<String> m = plugin.getConfig().getStringList(path);
            texto = "";
            for(int e = 0; e < m.size(); e++){
                texto += m.get(e);
                if(e + 1 < m.size()){
                    texto += "\n";
                }
            }
        }else{
            plugin.log("No es una lista.");
            texto = plugin.getConfig().getString(path);
        }
        enviar(p, texto, i);
    }
    public void mensaje(String p, String m, Object... i){
        if(plugin.getServer().getPlayer(p) == null){
            return;
        }
        mensaje(plugin.getServer().getPlayer(p), m, i);
    }
}
