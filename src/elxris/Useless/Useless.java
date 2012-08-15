package elxris.Useless;

import java.util.logging.Logger;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.MemoryConfiguration;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import elxris.Useless.Utils.Experiencia;
 
public class Useless extends JavaPlugin {
    FileConfiguration fc;
    MemoryConfiguration cache;
    Logger lggr;
    //ArrayList warps = new ArrayList();
    public void onEnable(){
        fc = this.getConfig();
        lggr = this.getLogger();
        checkConfiguration();
        cache = new MemoryConfiguration();
    }
    
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
        Player jugador;
        if(sender instanceof Player){
            jugador = (Player) sender;
        }else{
            return false;
        }
        if(cmd.getName().equalsIgnoreCase("tw")){
            if(jugador.hasPermission("Useless.tw")){
                if(!cache.isSet(jugador.getName()+".tw")){
                    cache.set(jugador.getName()+".tw", false);
                }
                if(!cache.getBoolean(jugador.getName()+".tw")){
                    String tiempo = fc.getString("tw.v.maxTime");;
                    if(args.length > 0){
                        if(Integer.parseInt(args[0]) <= fc.getInt("tw.v.maxTime")){
                            tiempo = args[0];
                            if(Integer.parseInt(args[0]) < fc.getInt("tw.v.minTime")){
                                tiempo = fc.getString("tw.v.minTime");
                            }
                        }
                    }else{
                        return false;
                    }
                    int precio = fc.getInt("tw.v.price")*Integer.parseInt(tiempo);
                    if(Experiencia.cobrarEsperiencia(jugador, precio)){
                        Warp w = new Warp(jugador.getLocation(), jugador, tiempo, cache, fc, getServer());
                        Thread t = new Thread(w);
                        t.start();
                        cache.set(jugador.getName()+".w", w.loc);
                        jugador.sendMessage(String.format(fc.getString("tw.s.created"), Integer.parseInt(tiempo), precio));
                    }else{
                        jugador.sendMessage(fc.getString("tw.s.noMoney"));
                    }
                }else{
                    jugador.teleport((Location)cache.get(jugador.getName()+".w"));
                }
            }
            return true;
        }
        return false;
    }

    public void checkConfiguration(){
        String path;
        path = "version";
        if(!this.getConfig().isSet(path))
            this.getConfig().set(path, Double.parseDouble(this.getDescription().getVersion()));
        path = "tw.s.created";
        if(!this.getConfig().isSet(path))
            this.getConfig().set(path, "Warp Temporal de %d minutos, y te costo %d puntos de experiencia.");
        path = "tw.s.remain";
        if(!this.getConfig().isSet(path))
            this.getConfig().set(path, "Quedan %d segundos.");
        path = "tw.s.teleported";
        if(!this.getConfig().isSet(path))
            this.getConfig().set(path, "Teleportado.");
        path = "tw.s.destroyed";
        if(!this.getConfig().isSet(path))
            this.getConfig().set(path, "Warp Destruido.");
        path = "tw.s.noMoney";
        if(!this.getConfig().isSet(path))
            this.getConfig().set(path, "No puedes pagarte un warp temporal en este momento.");
        path = "tw.v.maxTime";
        if(!this.getConfig().isSet(path))
            this.getConfig().set(path, 15);
        path = "tw.v.minTime";
        if(!this.getConfig().isSet(path))
            this.getConfig().set(path, 1);
        path = "tw.v.price";
        if(!this.getConfig().isSet(path))
            this.getConfig().set(path, 10);
        
        this.saveConfig();
    }
}