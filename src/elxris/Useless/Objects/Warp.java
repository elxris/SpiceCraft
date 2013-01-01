package elxris.Useless.Objects;

import org.bukkit.Location;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;
import elxris.Useless.Utils.Chat;

public class Warp implements Runnable{
    Location loc;
    Player jugador;
    int tiempo;
    Configuration cache;
    String path;
    
    public Warp(Location location, Player jugador, String tiempo, Configuration cache, String path){
        setLocation(location);
        setJugador(jugador);
        setTiempo(tiempo);
        setCache(cache);
    }
    public void setLocation(Location l){
        loc = l;
    }
    
    public Location getLocation(){
        return loc;
    }
    
    public void setJugador(Player p){
        jugador = p;
    }
    public Player getJugador() {
        return jugador;
    }
    public String getJugadorName() {
        return getJugador().getName();
    }
    public void setPath(String path) {
        this.path = path;
    }
    public String getPath(String jugador) {
        return String.format(path, jugador);
    }
    public void setTiempo(String t){
        tiempo = Integer.parseInt(t);
    }
    
    public void setCache(Configuration c){
        cache = c;
    }
    @Override
    public void run() {
        try {
            cache.set(getPath(getJugadorName())+".set", true);
            int t = tiempo*1000*60;
            t -= (1000*30);
            if(t < 1){
                t = 1;
            }
            Thread.sleep(t);
            for(int i = 6;i > 0;i--){
                Chat.mensaje(jugador, "tw.s.remain", i*5);
                Thread.sleep(5*1000);
            }
            Chat.mensaje(jugador, "tw.s.destroyed");
            cache.set(getPath(getJugadorName())+".set", false);
        } catch (Throwable e) {
        }
    }
}
