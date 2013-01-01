package elxris.Useless.Commands;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import elxris.Useless.Utils.Archivo;

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
        if(args.length < 0){
            mensaje(jugador, "mbox.info");
            return false;
        }
        jugador.setCompassTarget(new Location(jugador.getWorld(), Double.parseDouble(args[1]), Double.parseDouble(args[2]), Double.parseDouble(args[3])));
        return true;
    }
    
    private void setFile(Archivo file) {
        this.file = file;
    }
    
    private Archivo getFile() {
        return file;
    }
    
    private FileConfiguration getCache(){
        return cache;
    }
    
    private void loadCache(){
        cache = file.load();
    }
    
    private void saveCache(){
        file.save(cache);
    }
}