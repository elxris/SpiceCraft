package elxris.Useless.Commands;

import java.util.ArrayList;
import java.util.List;

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
            mensaje(jugador, "alert.permission");
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
        // COMANDOS PARA CREAR CORREOS.
        if(!jugador.hasPermission("useless.mail.create")){
            mail.interpreta();
            return true;
        }
        if(isCommand("comm.mbox.new", args[0])){
            if(args.length > 1){
                mail.createBorrador(jugador.getName(), cortarArray(1, args));              
            }
        }else if(isCommand("comm.mbox.add", args[0])){
            if(args.length > 1){
                mail.addMensaje(jugador.getName(), getString(cortarArray(1, args)));
            }
        }else if(isCommand("comm.mbox.send", args[0])){
            if(args.length == 1){
                mail.sendMensaje(jugador.getName());                
            }else if(args.length >= 3){
                List<String> destinatario = new ArrayList<String>();
                destinatario.add(args[1]);
                mail.sendMensaje(jugador.getName(), destinatario, getString(cortarArray(2, args)), false);
            }
            return true;
        }else if(isCommand("comm.mbox.sendall", args[0])){
            if(args.length == 1){
                mail.sendMensajeATodos(jugador.getName());
            }else if(args.length > 1){
                String mess = getString(cortarArray(1, args));
                mail.sendMensajeATodos(jugador.getName(), mess);
            }
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
