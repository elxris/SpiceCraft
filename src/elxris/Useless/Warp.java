package elxris.Useless;

import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.configuration.MemoryConfiguration;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class Warp implements Runnable{
    Location loc;
    Player jugador;
    int tiempo;
    MemoryConfiguration cache;
    FileConfiguration fc;
    Server server;
    
    public Warp(Location l, Player p, String t, MemoryConfiguration c, FileConfiguration fc, Server s){
        setLocation(l);
        setJugador(p);
        setTiempo(t);
        setCache(c);
        setFC(fc);
        setServer(s);
    }
    public void setLocation(Location l){
        loc = l;
    }
    
    public void setJugador(Player p){
        jugador = p;
    }
    
    public void setTiempo(String t){
        tiempo = Integer.parseInt(t);
    }
    
    public void setCache(MemoryConfiguration c){
        cache = c;
    }
    
    public void setFC(FileConfiguration f){
        fc = f;
    }
    
    public void setServer(Server s){
        server = s;
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
                server.getPlayer(jugador.getName()).sendMessage(String.format(fc.getString("tw.s.remain"), i*5));
                Thread.sleep(5*1000);
            }
            server.getPlayer(jugador.getName()).sendMessage(fc.getString("tw.s.destroyed"));
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
