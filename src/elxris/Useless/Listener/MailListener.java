package elxris.Useless.Listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;

import elxris.Useless.Objects.Mail;

public class MailListener implements Listener{
    public Plugin plugin;
    public Mail mail;
    
    public MailListener(Plugin p) {
        plugin = p;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        mail = new Mail(p);
    }
    
    @EventHandler
    public void onLogin(PlayerJoinEvent event){
        mail.getMailList(event.getPlayer().getName());
    }
}
