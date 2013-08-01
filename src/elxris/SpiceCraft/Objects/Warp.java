package elxris.SpiceCraft.Objects;

import org.bukkit.Location;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import elxris.SpiceCraft.SpiceCraft;
import elxris.SpiceCraft.Commands.WarpCommand;
import elxris.SpiceCraft.Utils.Archivo;
import elxris.SpiceCraft.Utils.Chat;

public class Warp implements Runnable{
    Location loc;
    String jugador;
    int tiempo;
    Archivo file;
    Configuration cache;
    String path;
    long date;
    
    public Warp(Archivo file, Configuration cache, String path){
        setFile(file);
        setCache(cache);
        setPath(path);
        setLocation(WarpCommand.getLocation(path));
        setJugador((String)get("owner"));
        setDate((long)get("date"));
        setTiempo((int)get("time"));
    }
    public Object get(String path){
        return getCache().get(getPath()+"."+path);
    }
    public void setLocation(Location l){
        loc = l;
    }
    public Location getLocation(){
        return loc;
    }
    public void setJugador(String p){
        jugador = p;
    }
    public Player getJugador() {
        return SpiceCraft.getOnlinePlayer(getJugadorName());
    }
    public String getJugadorName() {
        return jugador;
    }
    public void setPath(String path) {
        this.path = path;
    }
    public String getPath() {
        return path;
    }
    private void setTiempo(int t){
        tiempo = t;
    }
    private long getTiempo(){
        long t = tiempo;
        t -= ((System.currentTimeMillis() - getDate()));
        if(t < 0){
            return 0;
        }else{
            return t;
        }
    }
    private void setDate(long date){
        this.date = date;
    }
    private long getDate(){
        return date;
    }
    public void setCache(Configuration c){
        cache = c;
    }
    public Configuration getCache(){
        return cache;
    }
    public void setFile(Archivo file){
        this.file = file;
    }
    public Archivo getFile(){
        return file;
    }
    public void save(){
        getFile().save((FileConfiguration) getCache());
    }
    public void chat(String s, Object o){
        if(getJugador() != null){
            Chat.mensaje(getJugador(), s, o);
        }
    }
    @Override
    public void run() {
        try {
            getCache().set(getPath()+".set", true);
            long t = getTiempo();
            t -= (1000*30);
            if(t < 1){
                t = 1;
            }
            Thread.sleep(t); // Duerme hasta que queden 30 segundos.
            for(int i = 7;i > 0;){
                if(!getCache().isSet(getPath()+".set")){
                    break;
                }
                if(i == 7){
                    i--;
                    chat("tw.remain", 30);
                    Thread.sleep(15*1000); // Duerme 15 segundos.
                }else if(i == 6){
                    i--;
                    chat("tw.remain", 15);
                    Thread.sleep(10*1000);
                }else{
                    chat("tw.remain", i);
                    i--;
                    Thread.sleep(1000);
                }
            }
            if(getCache().isSet(getPath()+".set")){
                chat("tw.destroyed", null);
                getCache().set(getPath(), null);
                getCache().set("user."+getJugadorName(), cache.getInt("user."+getJugadorName())-1);
                save();
            }
        } catch (Throwable e) {
        }
    }
}
