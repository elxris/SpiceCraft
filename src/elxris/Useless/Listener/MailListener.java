package elxris.Useless.Listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import elxris.Useless.Objects.Mail;

public class MailListener implements Listener{
    public Mail mail;
    public MailListener(Mail m) {
        setMail(m);
    }
    @EventHandler
    public void onLogin(PlayerJoinEvent event){
        new MailDelay(event.getPlayer(), (long) 2, getMail());
    }
    public void setMail(Mail mail) {
        this.mail = mail;
    }
    public Mail getMail() {
        return mail;
    }
}
