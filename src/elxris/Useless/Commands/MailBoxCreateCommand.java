package elxris.Useless.Commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import elxris.Useless.Objects.Mail;

public class MailBoxCreateCommand extends Comando{
    private Mail mail;
    
    public MailBoxCreateCommand(Mail m) {
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
            mensaje(jugador, "mboxc.info");
            return true;
        }
        switch (args[0].toLowerCase()) {
        case "new":
        case "n":
            if(args.length > 1){
                mail.createBorrador(jugador.getName(), cortarArray(1, args));              
            }
            break;
        case "add":
        case "a":
            if(args.length > 1){
                mail.addMensaje(jugador.getName(), getString(cortarArray(1, args)));
            }
            break;
        case "clear":
        case "c":
            mail.clearMensaje(jugador.getName());
            break;
        case "send":
        case "s":
            mail.sendMensaje(jugador.getName());
            break;
        case "sendall":
        case "sa":
            mail.sendMensajeATodos(jugador.getName());
            break;
        case "help":
        case "?":
        default:
            mensaje(jugador, "mboxc.info");
            return true;
        }
        mail.interpreta();
        return true;
    }
    
    public String[] cortarArray(int id, String[] args){
        List<String> resultado = new ArrayList<String>();
        for(int i = id; i < args.length; i++){
            resultado.add(args[i]);
        }
        return resultado.toArray(new String[resultado.size()]);
    }
    
    public String getString(String[] args){
        String string = "";
        for(String k: args){
            string = string.concat(k);
            string = string.concat(" ");
        }
        return string;
    }
}
