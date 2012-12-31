package elxris.Useless.Objects;

import org.bukkit.Location;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;
import elxris.Useless.Useless;
import elxris.Useless.Utils.Chat;

public class Warp implements Runnable{
    Location loc;
    Player jugador;
    int tiempo;
    Configuration cache;
    Chat chat;
    
    public Warp(Location location, Player jugador, String tiempo, Configuration cache, Useless plugin){
        setLocation(location);
        setJugador(jugador);
        setTiempo(tiempo);
        setCache(cache);
        setChat(plugin);
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
    
    public void setTiempo(String t){
        tiempo = Integer.parseInt(t);
    }
    
    public void setCache(Configuration c){
        cache = c;
    }
    
    public void setChat(Useless plugin){
        chat = plugin.getChat();
    }
    public Chat getChat() {
        return chat;
    }
    @Override
    public void run() {
        try {
            cache.set(jugador.getName()+".tw", true);
            int t = tiempo*1000*60;
            t -= (1000*30);
            if(t < 1){
                t = 1;
            }
            Thread.sleep(t);
            for(int i = 6;i > 0;i--){
                getChat().mensaje(jugador, "tw.s.remain", i*5);
                Thread.sleep(5*1000);
            }
            getChat().mensaje(jugador, "tw.s.destroyed");
            cache.set(jugador.getName()+".tw", false);
        } catch (Throwable e) {
        }
    }
    
    public Player toPlayer(){
        return jugador;
    }
    
    public Location usar(){
        try {
            this.finalize();
        } catch (Throwable e) {
        }
        return loc;
    }
}
