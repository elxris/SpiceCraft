package elxris.Useless.Listener;

import org.bukkit.entity.Player;

import elxris.Useless.Objects.Mail;

public class MessageDelay implements Runnable{
    private Player jugador;
    private Mail mail;
    public MessageDelay(Player jugador, Mail mail) {
        setJugador(jugador);
        setMail(mail);
        Thread hilo = new Thread(this);
        hilo.start();
    }
    @Override
    public void run() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        getMail().getMailList(getJugador().getName());
    }
    public void setJugador(Player jugador) {
        this.jugador = jugador;
    }
    public Player getJugador() {
        return jugador;
    }
    public void setMail(Mail mail) {
        this.mail = mail;
    }
    public Mail getMail() {
        return mail;
    }
}
