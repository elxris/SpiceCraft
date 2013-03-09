package elxris.Useless.Listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import elxris.Useless.Useless;
import elxris.Useless.Objects.Mail;

public class MailListener implements Listener{
    public Mail mail;
    private boolean ONLOGIN;
    private long ONLOGINDELAY;
    public MailListener(Mail m) {
        setMail(m);
        ONLOGIN = Useless.plugin().getConfig().getBoolean("mail.onLogin");
        ONLOGINDELAY = Useless.plugin().getConfig().getLong("mail.onLoginDelay");
    }
    @EventHandler
    public void onLogin(PlayerJoinEvent event){
        if(!ONLOGIN){
            return;
        }
        if(event.getPlayer().hasPermission("useless.mail")){
            new MailDelay(event.getPlayer(), ONLOGINDELAY, getMail());
        }
    }
    public void setMail(Mail mail) {
        this.mail = mail;
    }
    public Mail getMail() {
        return mail;
    }
    private class MailDelay extends MessageDelay{
        private Mail mail;
        public MailDelay(Player jugador, Long tiempo, Mail mail) {
            super(jugador, "", tiempo*1000);
            setMail(mail);
            start();
        }
        public void setMail(Mail mail) {
            this.mail = mail;
        }
        public Mail getMail() {
            return mail;
        }
        @Override
        public void run() {
            super.run();
            getMail().getMailList(getJugador().getName());
        }
    }
}
