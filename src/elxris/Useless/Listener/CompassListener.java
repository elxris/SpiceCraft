package elxris.Useless.Listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class CompassListener implements Listener {
    @EventHandler
    public void onPLayerLogin(PlayerJoinEvent event){
        // TODO Guardar su punto, y relocalizarlo cuando entre.
    }
}
