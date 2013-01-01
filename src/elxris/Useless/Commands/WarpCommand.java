package elxris.Useless.Commands;

import net.milkbowl.vault.economy.Economy;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

import elxris.Useless.Useless;
import elxris.Useless.Objects.Warp;
import elxris.Useless.Utils.Chat;
import elxris.Useless.Utils.Experiencia;
import elxris.Useless.Utils.Strings;

public class WarpCommand extends Comando{
    private Configuration cache;
    private static Economy econ = null;
    
    public WarpCommand(Configuration cache) {
        this.cache = cache;
        if (!setupEconomy() ) {
            Useless.log(Strings.getString("alert.noEconomy"));
        }
        return;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player jugador;
        if(sender instanceof Player){
            jugador = (Player) sender;
        }else{
            return false;
        }
        if(!jugador.hasPermission("Useless.tw")){
            return false;
        }
        // Crea un registro en el cache, por si se va a usar.
        if(!cache.isSet(getPath("p.%s.set", jugador))){
            cache.set(getPath("p.%s.set", jugador), false);
        }
        
        // Si no hay argumentos. Shortcut, o info.
        if(args.length < 1){
            // Busca si hay un warp personal para el jugador.
            // Si no, muestra la info.
            if(cache.getBoolean(getPath("p.%s.set", jugador))){
                teleport(jugador, "p.%s.w");
            }else{
                Chat.mensaje(jugador, "tw.info", getPrecio(Strings.getDouble("tw.v.price")));
            }
        }else
        // Si hay un argumento. Busca el warp indicado.
        if(args.length == 1){
            // Si existe el warp teleporta.
            if(cache.isSet(getPath("g.%s.set", jugador))){
                teleport(jugador, "g.%s.set");
            }else{
                //No existe el warp
                Chat.mensaje(jugador, "tw.s.noExist");
            }
        } else
        
        // Si son dos argumentos.
        if(args.length == 2){
            if(args[0] == "new" || args[0] == "n"){
                
            }else{
                
            }
        } else
        if (args.length == 3){
            
        }
        
        // Recordar que si es general, poner el nombre, si no dejar el nombre vacío. [tw.s.created]
        // TODO Borrar esto.
        /*if(!cache.getBoolean(jugador.getName()+".tw")){
            String tiempo = Strings.getString("tw.v.maxTime");
            if(args.length > 0){
                if(Integer.parseInt(args[0]) <= Strings.getInt("tw.v.maxTime")){
                    tiempo = args[0];
                    if(Integer.parseInt(args[0]) < Strings.getInt("tw.v.minTime")){
                        tiempo = Strings.getString("tw.v.minTime");
                    }
                }
            }else{
                mensaje(jugador, "tw.info", Strings.getInt("tw.v.price"));
                return true;
            }
            int precio = Strings.getInt("tw.v.price")*Integer.parseInt(tiempo);
            if(Experiencia.cobrarEsperiencia(jugador, precio)){
                Warp w = new Warp(jugador.getLocation(), jugador, tiempo, cache, "p.%s");
                Thread t = new Thread(w);
                t.start();
                cache.set(jugador.getName()+".w", w.getLocation());
                mensaje(jugador, "tw.s.created", Integer.parseInt(tiempo));
            }else{
                mensaje(jugador, "tw.s.noMoney");
            }
        }else{
            jugador.teleport((Location)cache.get(jugador.getName()+".w"));
            mensaje(jugador, "tw.s.teleported");
        }*/
        // Hasta acá.
        return true;
    }
    public void teleport(Player jugador, String path){
        jugador.teleport((Location)cache.get(getPath(path, jugador)));
        mensaje(jugador, "tw.s.teleported");
    }
    public String getPath(String path, Player jugador) {
        return String.format(path, getPlayerName(jugador));
    }
    public String getPlayerName(Player jugador){
        return jugador.getName();
    }
    private boolean setupEconomy() {
        if (Useless.plugin().getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = Useless.plugin().getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }
    private String getPrecio(double precio){
        if(econ != null){
            return econ.format(precio);
        }
        // Si no
        return String.format(Strings.getString("exp.format"), (int)precio);
    }
}
