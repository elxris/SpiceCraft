package elxris.Useless.Commands;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;

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
                teleport(jugador, "p.%s.warp");
            }else{
                chatInfo(jugador);
            }
        }else
        // Si hay un argumento. Busca el warp indicado.
        if(args.length == 1){
            // Si existe el warp teleporta.
            if(cache.isSet(String.format("g.%s.set", args[0]))){
                teleport(jugador, String.format("g.%s.warp", args[0]));
            }else{
                //No existe el warp
                Chat.mensaje(jugador, "tw.s.noExist");
            }
        } else
        
        // Si son dos argumentos crea el warp personal.
        if(args.length == 2){
            if(args[0].contentEquals("new") || args[0].contentEquals("n")){
                String warpName = String.format("p.%s", getPlayerName(jugador));
                validarNewWarp(warpName, args[1], "", jugador);
                return true;
            }
            Chat.mensaje(jugador, "alert.error");
            return true;
        } else
            
        // Si son tres argumentos crea el warp general.
        if (args.length == 3){
            if(args[0].contentEquals("new") || args[0].contentEquals("n")){
                String warpName = String.format("g.%s", args[1]);
                validarNewWarp(warpName, args[2], args[1], jugador);
                return true;
            }
            Chat.mensaje(jugador, "alert.error");
            return true;
        }
        
        // Recordar que si es general, poner el nombre, si no dejar el nombre vacío. [tw.s.created]
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
    public void chatInfo(Player jugador){
        Chat.mensaje(jugador, "tw.info", getPrecio(Strings.getDouble("tw.v.price")));
    }
    public boolean crearWarp(Player jugador, int tiempo, String path){
        double precio = Strings.getDouble("tw.v.price")*tiempo;
        if(econ != null){
            if(econ.getBalance(getPlayerName(jugador)) >= precio){
                EconomyResponse r = econ.withdrawPlayer(getPlayerName(jugador), precio);                
                if(!r.transactionSuccess()){
                    mensaje(jugador, "alert.error");
                    return false;
                }
            }else{
                mensaje(jugador, "tw.s.noMoney");
                return false;
            }
        }else{
            if(Experiencia.cobrarEsperiencia(jugador, (int)precio)){
                mensaje(jugador, "tw.s.noMoney");
                return false;
            }
        }
        Chat.mensaje(jugador, "econ.cobrar", precio);
        Warp w = new Warp(jugador.getLocation(), jugador, tiempo, cache, path);
        Thread t = new Thread(w);
        t.start();
        cache.set(getPath(path, jugador)+".warp", w.getLocation());
        return true;
    }
    public void validarNewWarp(String path, String tiempo, String nombreWarp, Player jugador){
        // Si es entero.
        if(!isInteger(tiempo)){
            Chat.mensaje(jugador, "alert.noInteger");
            return;
        }
        // Si es ya existe.
        if(cache.isSet(path+".warp")){
            mensaje(jugador, "tw.s.exist");
            return;
        }
        int minutos = Integer.parseInt(tiempo);
        //Si está fuera de rango.
        if(!(minutos >= Strings.getInt("tw.v.minTime") && minutos <= Strings.getInt("tw.v.maxTime"))){
            mensaje(jugador, "tw.s.timeLimit", Strings.getInt("tw.v.minTime"), Strings.getInt("tw.v.maxTime"));
            return;
        }
        if(crearWarp(jugador, minutos, path)){            
            mensaje(jugador, "tw.s.created", minutos, nombreWarp);
        }
    }
    
    //Economía.
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
            if(precio < 1){
                return String.format("%s %s", econ.format(precio), econ.currencyNameSingular());                
            }else{
                return String.format("%s %s", econ.format(precio), econ.currencyNamePlural());
            }
        }
        // Si no
        return String.format(Strings.getString("exp.format"), (int)precio);
    }
}
