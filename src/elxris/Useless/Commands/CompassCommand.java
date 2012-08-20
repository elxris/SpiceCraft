package elxris.Useless.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;

import elxris.Useless.Useless;
import elxris.Useless.Objects.Mail;
import elxris.Useless.Utils.Chat;

public class CompassCommand implements CommandExecutor{
    private Useless plugin;
    private Configuration cache, fc;
    private Chat chat;
    
    public CompassCommand(Useless plugin, Configuration cache) {
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
            return true;
        }
        if(!jugador.hasPermission("useless.target")){
            return true;
        }
        switch (args[0]) {
        case "leer":
            
            break;
        case "help":
        case "?":
        case "":
        default:
            chat.mensaje(jugador, fc.getString("mbox.info"));
            return true;
        }
        return true;
    }
}