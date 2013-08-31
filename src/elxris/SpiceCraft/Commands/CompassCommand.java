package elxris.SpiceCraft.Commands;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import elxris.SpiceCraft.SpiceCraft;
import elxris.SpiceCraft.Utils.Archivo;
import elxris.SpiceCraft.Utils.Chat;
import elxris.SpiceCraft.Utils.Strings;

public class CompassCommand extends Comando{
    private Archivo file;
    private FileConfiguration cache;
    
    public CompassCommand() {
        setFile(new Archivo("pin.yml"));
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player jugador;
        if(sender instanceof Player){
            jugador = (Player) sender;
        }else{
            return true;
        }
        if(!jugador.hasPermission("spicecraft.upin")){
            mensaje(jugador, "alert.permission");
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
    private boolean isPinSet(Player p, String name){
        if(getCache().isSet("pin."+p.getName()+"."+name+".loc.world")){
            return true;
        }
        return false;
    }
    private void localizaPin(Player jugador, String name){
        String path = "pin."+jugador.getName()+"."+name+".loc";
        if(isPinSet(jugador, name)){
            World world = SpiceCraft.plugin().getServer().getWorld(getCache().getString(path+".world"));
            int x = getCache().getInt(path+".x");
            int z = getCache().getInt(path+".z");
            jugador.setCompassTarget((Location) new Location(world, x, 0, z));
            Chat.mensaje(jugador, "upin.set");
            return;
        }
        Chat.mensaje(jugador, "upin.noPin");
    }
    private void creaPin(String sX, String sZ, String name, Player jugador){
        if(isPinSet(jugador, name)){
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
        if(!getValidUserPins(jugador)){
            mensaje(jugador, "upin.limitPin");
            return;
        }
        String path = "pin."+jugador.getName()+"."+name;
        getCache().set(path+".loc.world", jugador.getWorld().getName());
        getCache().set(path+".loc.x", x);
        getCache().set(path+".loc.z", z);
        
        saveCache();
        mensaje(jugador, "upin.created", name);
    }
    private void borraPin(Player jugador, String name){
        if(isPinSet(jugador, name)){
            getCache().set("pin."+jugador.getName()+"."+name, null);
            saveCache();
            Chat.mensaje(jugador, "upin.del");
            return;
        }
        Chat.mensaje(jugador, "upin.noPin");
    }
    private void listaPin(Player jugador){
        String list = "";
        String path = "pin."+jugador.getName();
        if(!getCache().isSet(path)){
            Chat.mensaje(jugador, "upin.noList");
            return;
        }
        for(String s: getCache().getConfigurationSection(path).getKeys(false)){
            list += String.format(Strings.getString("upin.item"),
                    getCache().getString(path+"."+s+".loc.world").toUpperCase(),
                    s, getCache().getString(path+"."+s+".loc.x"),
                    getCache().getString(path+"."+s+".loc.z"));
        }
        if(list == ""){
            Chat.mensaje(jugador, "upin.noList");
            return;
        }
        Chat.mensaje(jugador, "upin.list", list);
    }
    private boolean getValidUserPins(Player jugador){
        int maxPerUser = getInt("upin.maxPerUser");
        if(maxPerUser == 0){
            return true;
        }
        if(jugador.hasPermission("spicecraft.upin.noLimit")){
            return true;
        }
        if(!getCache().isSet("pin."+jugador.getName())){
            return true;
        }
        if(getCache().getConfigurationSection("pin."+jugador.getName()).getKeys(false).size() < maxPerUser){
            return true;
        }
        return false;
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