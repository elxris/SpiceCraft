package elxris.Useless.Listener;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;

import elxris.Useless.Useless;
import elxris.Useless.Utils.Chat;

public class MessageDelay implements Runnable{
    private Player jugador;
    private List<String> mensaje;
    private long tiempo;
    private Thread hilo;
    public MessageDelay(Player jugador, List<String> mensaje, long tiempo) {
        setJugador(jugador);
        setMensaje(mensaje);
        setTiempo(tiempo);
        setHilo(new Thread(this));
    }
    public MessageDelay(Player jugador, String mensaje, long tiempo) {
        setJugador(jugador);
        List<String> m = new ArrayList<String>();
        m.add(mensaje);
        setMensaje(m);
        setTiempo(tiempo);
        setHilo(new Thread(this));
    }
    @Override
    public void run() {
        for(String s: getMensaje()){
            sleep();
            if(s != ""){
                Chat.mensaje(getJugador(), s);
            }
        }
    }
    public void sleep(){
        try {
            Thread.sleep(getTiempo());
        } catch (InterruptedException e) {
            Useless.log(":");
        }
    }
    public void start(){
        getHilo().start();
    }
    public void setJugador(Player jugador) {
        this.jugador = jugador;
    }
    public Player getJugador() {
        return jugador;
    }
    public void setHilo(Thread hilo) {
        this.hilo = hilo;
    }
    public Thread getHilo() {
        return hilo;
    }
    public void setMensaje(List<String> mensaje) {
        this.mensaje = mensaje;
    }
    public List<String> getMensaje() {
        return mensaje;
    }
    public void setTiempo(long tiempo) {
        this.tiempo = tiempo;
    }
    public long getTiempo() {
        return tiempo;
    }
}
