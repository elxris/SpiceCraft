package elxris.Useless.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import elxris.Useless.Objects.Mail;

public class MailBoxCommand extends Comando{
    private Mail mail;
    
    public MailBoxCommand(Mail m) {
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
        if(isCommand("comm.mbox.count", args[0])){
            mail.getMailList(jugador.getName());
        }else
        if(isCommand("comm.mbox.read", args[0])){
            mail.getNextMail(jugador.getName(), false);
        }else
        if(isCommand("comm.mbox.clear", args[0])){
            mail.eliminarAll(jugador.getName());
            mensaje(jugador, "mbox.deleted");
        }
        mail.interpreta();
        return true;
    }
}
