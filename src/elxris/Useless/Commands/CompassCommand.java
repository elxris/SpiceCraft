package elxris.Useless.Commands;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import elxris.Useless.Useless;
import elxris.Useless.Utils.Archivo;
import elxris.Useless.Utils.Chat;
import elxris.Useless.Utils.Strings;

public class CompassCommand extends Comando{
    // TODO Añadir la posibilidad de varios pins. Y accesibles a cualquiera.
    private Archivo file;
    private FileConfiguration cache;
    
    public CompassCommand(Archivo file) {
        setFile(file);
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player jugador;
        if(sender instanceof Player){
            jugador = (Player) sender;
        }else{
            return true;
        }
        if(!jugador.hasPermission("useless.upin")){
            return true;
        }
        if(args.length == 0){
            mensaje(jugador, "upin.info");
            return true;
        }
        // Localiza el pin.
        if(args.length == 1){
            if(isCommand("comm.upin.list", args[0])){
                listaPin(jugador);
            }else{
                localizaPin(jugador, args[0]);                
            }
        }else
        // Borra el pin.
        if(args.length == 2){
            if(isCommand("comm.upin.del", args[0])){
                borraPin(jugador, args[1]);
            }
        } else
        // Crea el pin.
        if(args.length == 3){
            creaPin(args[1], args[2], args[0], jugador);
        }
        return true;
    }
    private boolean isPinSet(String name){
        if(getCache().isSet("pin."+name+".loc.world")){
            return true;
        }
        return false;
    }
    private void localizaPin(Player jugador, String name){
        String path = "pin."+name+".loc";
        if(isPinSet(name)){
            World world = Useless.plugin().getServer().getWorld(getCache().getString(path+".world"));
            int x = getCache().getInt(path+".x");
            int z = getCache().getInt(path+".z");
            jugador.setCompassTarget((Location) new Location(world, x, 0, z));
            Chat.mensaje(jugador, "upin.set");
            return;
        }
        Chat.mensaje(jugador, "upin.noPin");
    }
    private void creaPin(String sX, String sZ, String name, Player jugador){
        if(isPinSet(name)){
            Chat.mensaje(jugador, "upin.exist");
            return;
        }
        if(jugador.getWorld().getEnvironment() == Environment.NETHER
                || jugador.getWorld().getEnvironment() == Environment.THE_END){
            Chat.mensaje(jugador, "upin.nether");
            return;
        }
        double x, z;
        if(isDouble(sX) && isDouble(sZ)){
            x = Double.parseDouble(sX);
            z = Double.parseDouble(sZ);
        }else{
            mensaje(jugador, "alert.error");
            return;
        }
        getCache().set("pin."+name+".loc.world", jugador.getWorld().getName());
        getCache().set("pin."+name+".loc.x", x);
        getCache().set("pin."+name+".loc.z", z);
        getCache().set("pin."+name+".player", jugador.getName());
        List<String> pinNames = getCache().getStringList("pins");
        pinNames.add(name);
        getCache().set("pins", pinNames);
        saveCache();
        mensaje(jugador, "upin.created", name);
    }
    private void borraPin(Player jugador, String name){
        if(isPinSet(name) && getCache().getString("pin."+name+".player").contentEquals(jugador.getName())){
            List<String> pinNames = getCache().getStringList("pins");
            pinNames.remove(name);
            getCache().set("pins", pinNames);
            getCache().set("pin."+name, null);
            saveCache();
            Chat.mensaje(jugador, "upin.del");
            return;
        }
        Chat.mensaje(jugador, "upin.noPin");
    }
    private void listaPin(Player jugador){
        String list = "";
        for(String s: getCache().getStringList("pins")){
            list += String.format(Strings.getString("upin.item"), s);
        }
        if(list == ""){
            Chat.mensaje(jugador, "upin.noList");
            return;
        }
        Chat.mensaje(jugador, "upin.list", list);
    }
    private void setFile(Archivo file) {
        this.file = file;
        loadCache();
    }
    
    private Archivo getFile() {
        return file;
    }
    
    private FileConfiguration getCache(){
        return cache;
    }
    
    private void loadCache(){
        cache = getFile().load();
    }
    
    private void saveCache(){
        getFile().save(cache);
    }
}