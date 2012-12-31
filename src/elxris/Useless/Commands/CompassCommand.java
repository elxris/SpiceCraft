package elxris.Useless.Commands;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;

import elxris.Useless.Utils.Chat;

public class CompassCommand extends Comando{
    private Configuration cache;
    
    public CompassCommand(Chat chat, Configuration cache) {
        super(chat);
        this.cache = cache;
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
        String arg = "";
        if(args.length > 0){
            arg = args[0];
        }
        switch (arg) {
        case "set":
            jugador.setCompassTarget(new Location(jugador.getWorld(), Double.parseDouble(args[1]), Double.parseDouble(args[2]), Double.parseDouble(args[3])));
            break;
        case "help":
        case "?":
        default:
            mensaje(jugador, "mbox.info");
            return true;
        }
        return true;
    }
}