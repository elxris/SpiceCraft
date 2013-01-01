package elxris.Useless.Listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import elxris.Useless.Useless;
import elxris.Useless.Objects.Mail;

public class MailListener implements Listener{
    public Mail mail;
    
    public MailListener(Mail m) {
        Useless.plugin().getServer().getPluginManager().registerEvents(this, Useless.plugin());
        mail = m;
    }
    
    @EventHandler
    public void onLogin(PlayerJoinEvent event){
        mail.getMailList(event.getPlayer().getName());
    }
}
