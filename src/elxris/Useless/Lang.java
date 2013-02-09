package elxris.Useless;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import elxris.Useless.Commands.Comando;

public class Lang extends Comando{

    @Override
    public boolean onCommand(CommandSender jugador, Command comando, String label,
            String[] args) {
        Player p = null;
        if(jugador instanceof Player){
            p = (Player) jugador;
        }else{
            return false;
        }
        if(args.length > 0){
            if(new CheckConfiguration().changeLang(args[0])){
                mensaje(p, "s", "Changed Lang of Useless!");
            }
        }
        return true;
    }
    
}
