package elxris.Useless.Commands;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;

import elxris.Useless.Useless;
import elxris.Useless.Objects.Warp;
import elxris.Useless.Utils.Chat;
import elxris.Useless.Utils.Experiencia;

public class WarpCommand implements CommandExecutor{
    private Useless plugin;
    private Configuration cache, fc;
    private Chat chat;
    
    public WarpCommand(Useless plugin, Configuration cache) {
        this.plugin = plugin;
        fc = plugin.getConfig();
        this.cache = cache;
        chat = new Chat(plugin.getServer());
    }
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player jugador;
        if(sender instanceof Player){
            jugador = (Player) sender;
        }else{
            return false;
        }
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
                    chat.mensaje(jugador, fc.getString("tw.info"));
                    return true;
                }
                int precio = fc.getInt("tw.v.price")*Integer.parseInt(tiempo);
                if(Experiencia.cobrarEsperiencia(jugador, precio)){
                    Warp w = new Warp(jugador.getLocation(), jugador, tiempo, cache, plugin);
                    Thread t = new Thread(w);
                    t.start();
                    cache.set(jugador.getName()+".w", w.getLocation());
                    chat.mensaje(jugador, fc.getString("tw.s.created"), Integer.parseInt(tiempo), precio);
                }else{
                    chat.mensaje(jugador, fc.getString("tw.s.noMoney"));
                }
            }else{
                jugador.teleport((Location)cache.get(jugador.getName()+".w"));
            }
        }
        return true;
    }

}
