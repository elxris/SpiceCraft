package elxris.SpiceCraft.Objects;

import org.bukkit.Location;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;

import elxris.SpiceCraft.Utils.Chat;

public class Warp implements Runnable{
    Location loc;
    Player jugador;
    int tiempo;
    Configuration cache;
    String path;
    
    public Warp(Location location, Player jugador, int tiempo, Configuration cache, String path){
        setLocation(location);
        setJugador(jugador);
        setTiempo(tiempo);
        setCache(cache);
        setPath(path);
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
    public String getPath() {
        return path;
    }
    public void setTiempo(int t){
        tiempo = t;
    }
    public void setCache(Configuration c){
        cache = c;
    }
    @Override
    public void run() {
        try {
            cache.set(getPath()+".set", true);
            cache.set("user."+jugador.getName(), cache.getInt("user."+jugador.getName())+1);
            int t = tiempo*1000*60;
            t -= (1000*30);
            if(t < 1){
                t = 1;
            }
            Thread.sleep(t); // Duerme hasta que queden 30 segundos.
            for(int i = 7;i > 0;){
                if(!cache.isSet(getPath()+".set")){
                    break;
                }
                if(i == 7){
                    i--;
                    Chat.mensaje(jugador, "tw.remain", 30);
                    Thread.sleep(15*1000); // Duerme 15 segundos.
                }else if(i == 6){
                    i--;
                    Chat.mensaje(jugador, "tw.remain", 15);
                    Thread.sleep(10*1000);
                }else{
                    Chat.mensaje(jugador, "tw.remain", i);
                    i--;
                    Thread.sleep(1000);
                }
            }
            if(cache.isSet(getPath()+".set")){
                Chat.mensaje(jugador, "tw.destroyed");
                cache.set(getPath(), null);
                cache.set("user."+jugador.getName(), cache.getInt("user."+jugador.getName())-1);
            }
        } catch (Throwable e) {
        }
    }
}
