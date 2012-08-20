package elxris.Useless.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;

import elxris.Useless.Useless;
import elxris.Useless.Objects.Mail;
import elxris.Useless.Utils.Chat;

public class MailBoxCommand implements CommandExecutor{
    private Useless plugin;
    private Configuration fc;
    private Chat chat;
    private Mail mail;
    
    public MailBoxCommand(Useless plugin, Mail m) {
        this.plugin = plugin;
        fc = this.plugin.getConfig();
        chat = new Chat(plugin.getServer());
        mail = m;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player jugador;
        if(sender instanceof Player){
            jugador = (Player) sender;
        }else{
            return true;
        }
        if(!jugador.hasPermission("useless.mail")){
            return true;
        }
        if(args.length < 1){
            chat.mensaje(jugador, fc.getString("mbox.info"));
            return true;
        }
        switch (args[0].toLowerCase()) {
        case "list":
            mail.getMailList(jugador.getName());
            break;
        case "next":
            mail.getNextMail(jugador.getName());
            break;
        case "keep":
            mail.keepMail(jugador.getName());
            break;
        case "help":
        case "?":
        default:
            chat.mensaje(jugador, fc.getString("mbox.info"));
            return true;
        }
        return true;
    }
}
