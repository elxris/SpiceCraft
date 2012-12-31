package elxris.Useless;

import java.util.logging.Logger;

import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.MemoryConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import elxris.Useless.Commands.CompassCommand;
import elxris.Useless.Commands.MailBoxCommand;
import elxris.Useless.Commands.MailBoxCreateCommand;
import elxris.Useless.Commands.WarpCommand;
import elxris.Useless.Listener.MailListener;
import elxris.Useless.Objects.Mail;
import elxris.Useless.Utils.Chat;
 
public class Useless extends JavaPlugin {
    private Configuration warpcache, pincache;
    private Mail mail;
    private Chat chat;
    private static Logger lggr;
    
    public void onEnable(){
        lggr = this.getLogger();
        checkConfiguration();
        chat = new Chat(this);
        //Commando Tempowal Warp
        warpcache = new MemoryConfiguration();
        getCommand("tw").setExecutor(new WarpCommand(chat, this, warpcache));
        //Comando Mail
        mail = new Mail(this);
        getCommand("mbox").setExecutor(new MailBoxCommand(chat, mail));
        getCommand("mboxc").setExecutor(new MailBoxCreateCommand(chat, mail));
        //Listener Mail
        new MailListener(this, mail);
        //Comando Compass
        pincache = new MemoryConfiguration();
        getCommand("upin").setExecutor(new CompassCommand(chat, pincache));
    }
    public void checkConfiguration(){
        String path;
        path = "version";
        if(!this.getConfig().isSet(path))
            this.getConfig().set(path, Double.parseDouble(this.getDescription().getVersion()));
        path = "string";
        if(!this.getConfig().isSet(path))
            this.getConfig().set(path, "%s");
        // Warps
        path = "tw.info";
        if(!this.getConfig().isSet(path))
            this.getConfig().set(path,
                    "/tw [minutos] Recuerda que por cada minuto te cobrará %d puntos de experiencia.");
        path = "tw.s.created";
        String[] s167 = {"Warp Temporal de %d minutos.",
                "Usa §l/tw§r para usarlo."};
        if(!this.getConfig().isSet(path))
            this.getConfig().set(path, s167);
        path = "tw.s.remain";
        if(!this.getConfig().isSet(path))
            this.getConfig().set(path, "Quedan §4%d§r segundos para la destrucción de el warp temporal.");
        path = "tw.s.teleported";
        if(!this.getConfig().isSet(path))
            this.getConfig().set(path, "Teleportado.");
        path = "tw.s.destroyed";
        if(!this.getConfig().isSet(path))
            this.getConfig().set(path, "Warp Destruido.");
        path = "tw.s.noMoney";
        if(!this.getConfig().isSet(path))
            this.getConfig().set(path, "No puedes pagarte un warp temporal en este momento.");
        path = "tw.v.maxTime";
        if(!this.getConfig().isSet(path))
            this.getConfig().set(path, 120);
        path = "tw.v.minTime";
        if(!this.getConfig().isSet(path))
            this.getConfig().set(path, 1);
        path = "tw.v.price";
        if(!this.getConfig().isSet(path))
            this.getConfig().set(path, 10);
        // Mail
        path = "mbox.info";
        String[] s = {"§aAyuda /mbox§r",
                "§l/mbox . §rListar los correos que tienes.",
                "§l/mbox §cR§r§lead §rLeer el correo.",
                "§l/mbox §cC§r§llear §rBorrar correo.",
                "§l/mboxc §rAyuda para crear correos."};
        if(!this.getConfig().isSet(path))
            this.getConfig().set(path, s);
        path = "mbox.list";
        if(!this.getConfig().isSet(path))
            this.getConfig().set(path, "Tienes §e%d§r mensajes. §e/mbox§r Para más info.");
        path = "mbox.mail";
        String[] s2 = {"De: %s Fecha: %s",
                "§l%s§r",
                "§e§l/mbox§r Ayuda. §e§l/mboxc create %s§r Responder"};
        if(!this.getConfig().isSet(path))
            this.getConfig().set(path, s2);
        path = "mbox.listEnd";
        if(!this.getConfig().isSet(path))
            this.getConfig().set(path, "Ya no tienes correos.");
        path = "mbox.deleted";
        if(!this.getConfig().isSet(path))
            this.getConfig().set(path, "Mensajes eliminados.");
        // Create mail
        path = "mboxc.info";
        String[] s3 = {"§aAyuda /mboxc§r",
                "Para crear un correo debes primero hacer un §l/mboxc create§r para después agregar el mensaje.",
                "Al usar el comando se crea un borrador con destinatario igual al remitente.",
                "§l/mboxc Create [Usuario] ... §rCrea un borrador con destinatario o destinatarios. Uno mínimo.",
                "§l/mboxc Add [Mensaje] §rAgrega el mensaje a el correo.",
                "§l/mboxc Clear §rBorra el mensaje.",
                "§l/mboxc Send §rYa que tienes listo el mensaje, este comando lo envía.",
                "§l/mboxc SendAll §r(Solo Admins)Envía el mensaje a todos los usuarios."};
        if(!this.getConfig().isSet(path))
            this.getConfig().set(path, s3);
        path = "mboxc.noPlayerAdded";
        if(!this.getConfig().isSet(path))
            this.getConfig().set(path, "Ningún destinatario, por lo tanto mensaje no creado.");
        path = "mboxc.playerNotExist";
        if(!this.getConfig().isSet(path))
            this.getConfig().set(path, "El jugador %s no existe. No ha sido agregado.");
        path = "mboxc.catched";
        if(!this.getConfig().isSet(path))
            this.getConfig().set(path, "Mensaje recibido.");
        path = "mboxc.sended";
        if(!this.getConfig().isSet(path))
            this.getConfig().set(path, "Mensaje enviado.");
        path = "mboxc.limit";
        if(!this.getConfig().isSet(path))
            this.getConfig().set(path, "Lo siento, ya has superado el límite de 300 caracteres.");
        path = "mboxc.created";
        if(!this.getConfig().isSet(path))
            this.getConfig().set(path, "Borrador creado. §e/mboxc add <Mensaje>§r para añadir mensaje.");
        path = "mboxc.add";
        if(!this.getConfig().isSet(path))
            this.getConfig().set(path, "Mensaje añadido, recuerda que puedes añadir más. §e/mboxc send§r para enviar.");
        // Compass
        path = "upin.info";
        String[] s548 = {"§aAyuda /upin§r", 
                "Para crear un "};
        if(!this.getConfig().isSet(path))
            this.getConfig().set(path, s548);
        // Errores
        path = "alert.notsaved";
        if(!this.getConfig().isSet(path))
            this.getConfig().set(path, "Error, no se ha podido guardar: ");
        // Formatos
        path = "f.units";
        if(!this.getConfig().isSet(path))
            this.getConfig().set(path, "mes meses dia dias hora horas minuto minutos segundo segundos");
        path = "f.months";
        if(!this.getConfig().isSet(path))
            this.getConfig().set(path, "Enero Febrero Marzo Abril Mayo Junio Julio Agosto Septiembre Octubre Noviembre Diciembre");
        path = "exp.cobrar";
        if(!this.getConfig().isSet(path))
            this.getConfig().set(path, "Cobrando %d puntos de experiencia.");
        /*path = "f.";
        if(!this.getConfig().isSet(path))
            this.getConfig().set(path, "");*/
        this.saveConfig();
    }
    public Chat getChat() {
        return chat;
    }
    public void log(String m){
        lggr.info(m);
    }
}