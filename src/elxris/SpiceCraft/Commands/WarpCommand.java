package elxris.SpiceCraft.Commands;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import elxris.SpiceCraft.SpiceCraft;
import elxris.SpiceCraft.Objects.Warp;
import elxris.SpiceCraft.Utils.Archivo;
import elxris.SpiceCraft.Utils.Chat;
import elxris.SpiceCraft.Utils.Fecha;
import elxris.SpiceCraft.Utils.Strings;

public class WarpCommand extends Comando implements Listener{
    private static Archivo file;
    private static Configuration cache;
    public WarpCommand() {
        init();
    }
    private void init(){
        // Cargar los personales.
        for(String s: new String[]{"p", "g"}){
            if(getCache().isSet(s)){
                for(String k: getCache().getConfigurationSection(s).getKeys(false)){
                    if(getCache().getBoolean(String.format(s+".%s.set", k))){
                        getCache().set(String.format(s+".%s.date", k), System.currentTimeMillis());
                        Thread t = new Thread(new Warp(file, getCache(), s+"."+k));
                        t.start();
                    }
                }
            }
        }
    }
    private static Archivo getFile() {
        if(file == null){
            setFile(new Archivo("warps.yml"));
        }
        return file;
    }
    private static void setFile(Archivo file) {
        WarpCommand.file = file;
    }
    private static Configuration getCache() {
        if(cache == null){
            setCache(getFile().load());
        }
        return cache;
    }
    private static void setCache(Configuration cache) {
        WarpCommand.cache = cache;
    }
    @EventHandler
    public void onDisable(PluginDisableEvent event){
        if(event.getPlugin() != SpiceCraft.plugin()){
            return;
        }
        for(String s: new String[]{"p", "g"}){
            if(getCache().isSet(s)){
                for(String k: getCache().getConfigurationSection(s).getKeys(false)){
                    if(getCache().getBoolean(String.format(s+".%s.set", k))){
                        long time = getCache().getInt(String.format(s+".%s.time", k));
                        long date = getCache().getLong(String.format(s+".%s.date", k));
                        long now = System.currentTimeMillis();
                        time = time - (now-date);
                        getCache().set(String.format(s+".%s.time", k), time);
                    }
                }
            }
        }
        file.saveNow(getCache());
    }
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player jugador;
        if(sender instanceof Player){
            jugador = (Player) sender;
        }else{
            return true;
        }
        if(!jugador.hasPermission("spicecraft.tw")){
            mensaje(jugador, "alert.permission");
            return true;
        }
        // Crea un registro en el cache, por si se va a usar.
        if(!getCache().isSet(getPath("p.%s.set", jugador))){
            getCache().set(getPath("p.%s.set", jugador), false);
        }
        
        // Si no hay argumentos. Shortcut, o info.
        if(args.length < 1){
            // Busca si hay un warp personal para el jugador.
            // Si no, muestra la info.
            if(getCache().getBoolean(getPath("p.%s.set", jugador))){
                teleport(jugador, "p.%s");
            }else{
                chatInfo(jugador);
            }
        }else
        // Si hay un argumento. Busca el warp indicado. O si llamó a destruir, destruye.
        if(args.length == 1){
            // Si existe el warp teleporta.
            if(getCache().isSet(String.format("g.%s.set", args[0]))){
                teleport(jugador, String.format("g.%s", args[0]));
                return true;
            }else if(isCommand("comm.tw.destroy", args[0])){
                borrarWarp(jugador, getPath("p.%s", jugador));
                return true;
            }else if(isCommand("comm.tw.list", args[0])){
                mensaje(jugador, "tw.listHeader", getlistaWarps());
                return true;
            }
            Chat.mensaje(jugador, "tw.noExist");
        } else
        
        // Si son dos argumentos crea el warp personal.
        if(args.length == 2){
            if(isCommand("comm.tw.new", args[0])){
                String warpName = getPath("p.%s", jugador);
                validarNewWarp(warpName, args[1], "", jugador);
                return true;
            }else if(isCommand("comm.tw.destroy", args[0])){
                borrarWarp(jugador, "g."+args[1]);
                return true;
            }
            Chat.mensaje(jugador, "alert.error");
            return true;
        } else
            
        // Si son tres argumentos crea el warp general.
        if (args.length == 3){
            if(isCommand("comm.tw.new", args[0])){
                String warpName = String.format("g.%s", args[1]);
                validarNewWarp(warpName, args[2], args[1], jugador);
                return true;
            }
            Chat.mensaje(jugador, "alert.error");
        }
        return true;
    }
    public Object get(String path){
        return getCache().get(path);
    }
    private void teleport(Player jugador, String path){
        double precio = getDouble("tw.usePrice");
        if(!getEcon().cobrar(jugador, precio)){
            mensaje(jugador, "tw.noMoney");
        }else{
            getEcon().getLogg().logg("TW", jugador, "pay for", "teleporting", 0, precio);
            jugador.teleport(getLocation(getPath(path, jugador)));
            mensaje(jugador, "tw.teleported");
        }
    }
    public static Location getLocation(String path){
        World w = SpiceCraft.plugin().getServer().getWorld(getCache().getString(path+".world"));
        Location l = ((Vector)getCache().get(path+".warp")).toLocation(w);
        l.setPitch((float)getCache().getDouble(path+".pitch"));
        l.setYaw((float)getCache().getDouble(path+".yaw"));
        return l;
    }
    private String getPath(String path, Player jugador) {
        return String.format(path, getPlayerName(jugador));
    }
    private String getPlayerName(Player jugador){
        return jugador.getName();
    }
    private void chatInfo(Player jugador){
        Chat.mensaje(jugador, "tw.info", getEcon().getPrecio(getDouble("tw.price")));
    }
    private void crearWarp(Player jugador, int tiempo, String path){
        getCache().set("user."+jugador.getName(), getCache().getInt("user."+jugador.getName())+1);
        getCache().set(path+".warp", jugador.getLocation().toVector());
        getCache().set(path+".world", jugador.getWorld().getName());
        getCache().set(path+".pitch", jugador.getLocation().getPitch());
        getCache().set(path+".yaw", jugador.getLocation().getYaw());
        getCache().set(path+".owner", jugador.getName());
        getCache().set(path+".date", System.currentTimeMillis());
        getCache().set(path+".time", tiempo*60*1000);
        Warp w = new Warp(file, getCache(), path);
        Thread t = new Thread(w);
        t.start();
        save();
        return;
    }
    private void validarNewWarp(String path, String tiempo, String nombreWarp, Player jugador){
        // Si es entero.
        if(!isInteger(tiempo)){
            Chat.mensaje(jugador, "alert.noInteger");
            return;
        }
        // Si es ya existe.
        if(getCache().isSet(path+".warp")){
            mensaje(jugador, "tw.exist");
            return;
        }
        int minutos = Integer.parseInt(tiempo);
        //Si está fuera de rango.
        if(!(minutos >= getInt("tw.minTime") && minutos <= getInt("tw.maxTime"))){
            if(!jugador.hasPermission("spicecraft.tw.noMaxTime")){
                mensaje(jugador, "tw.timeLimit", getInt("tw.minTime"), getInt("tw.maxTime"));
                return;                
            }
        }
        // Asegura que exista el numero de warps.
        if(!getCache().isSet("user."+getPlayerName(jugador))){
            getCache().set("user."+getPlayerName(jugador), 0);
        }
        // Si excede a los máximos por usuario.
        if(getInt("tw.maxPerUser") <= getCache().getInt("user."+getPlayerName(jugador))){
            if(!jugador.hasPermission("spicecraft.tw.noWarpLimit")){
                mensaje(jugador, "tw.warpLimit");
                return;
            }
        }
        double precio = getDouble("tw.price")*(double)minutos;
        if(!getEcon().cobrar(jugador, precio)){
            mensaje(jugador, "tw.noMoney");
            return;
        }
        getEcon().getLogg().logg("TW", jugador, "create", "minutes", minutos, precio);
        crearWarp(jugador, minutos, path);
        mensaje(jugador, "tw.created", minutos, nombreWarp);
    }
    private void borrarWarp(Player jugador, String path){
        if(getCache().isSet(path+".owner")){
            if(getCache().getString(path+".owner").contentEquals(jugador.getName())
                    || jugador.hasPermission("spicecraft.tw.master")){
                getCache().set(path, null);
                getCache().set("user."+jugador.getName(), getCache().getInt("user."+jugador.getName())-1);
                Chat.mensaje(jugador, "tw.destroyed");
                save();
                return;
            }else{
                mensaje(jugador, "tw.noOwner");
                return;
            }
        }
        mensaje(jugador, "tw.noExist");
    }
    private String getlistaWarps(){
        String r = "";
        if(getCache().isSet("g")){
            for(String s: getCache().getConfigurationSection("g").getKeys(false)){
                if(getCache().getBoolean(String.format("g.%s.set", s))){
                    r += String.format(
                            Strings.getString("tw.listItem")+"\n", 
                            s,
                            getCache().getString("g."+s+".owner"),
                            Fecha.parseTiempo((getCache().getLong("g."+s+".date")+cache.getLong("g."+s+".time")-System.currentTimeMillis())/1000)
                    );
                }
            }
        }
        return r;
    }
    private void save(){
        getFile().save(getCache());
    }
}
