package elxris.Useless.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import elxris.Useless.Objects.Mail;
import elxris.Useless.Utils.Chat;

public class MailBoxCommand extends Comando{
    private Mail mail;
    
    public MailBoxCommand(Chat chat, Mail m) {
        super(chat);
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
            mensaje(jugador, "mbox.info");
            return true;
        }
        //TODO Crear comandos moderadores.
        switch (args[0].toLowerCase()) {
        case ".":
            mail.getMailList(jugador.getName());
            break;
        case "read":
            mail.getNextMail(jugador.getName(), false);
            break;
        case "..":
            mail.eliminarAll(jugador.getName());
            mensaje(jugador, "mbox.deleted");
            break;
        case "help":
        case "?":
        default:
            mensaje(jugador, "mbox.info");
            return true;
        }
        mail.interpreta();
        return true;
    }
}
