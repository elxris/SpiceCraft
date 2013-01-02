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
        setMail(m);
    }
    
    @EventHandler
    public void onLogin(PlayerJoinEvent event){
        new MessageDelay(event.getPlayer(), getMail());
    }
    public void setMail(Mail mail) {
        this.mail = mail;
    }
    public Mail getMail() {
        return mail;
    }
}
