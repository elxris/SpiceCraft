package elxris.Useless.Listener;

import org.bukkit.entity.Player;

import elxris.Useless.Objects.Mail;

public class MailDelay extends MessageDelay{
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
