package elxris.SpiceCraft.Listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class CompassListener implements Listener {
    @EventHandler
    public void onPlayerLogin(PlayerJoinEvent event){
        // TODO Guardar su punto, y relocalizarlo cuando entre.
    }
    @EventHandler
    public void onPlayerLogout(PlayerQuitEvent event){
        
    }
}
