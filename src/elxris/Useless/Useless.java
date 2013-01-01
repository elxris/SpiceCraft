package elxris.Useless;

import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.MemoryConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import elxris.Useless.Commands.CompassCommand;
import elxris.Useless.Commands.MailBoxCommand;
import elxris.Useless.Commands.MailBoxCreateCommand;
import elxris.Useless.Commands.WarpCommand;
import elxris.Useless.Listener.MailListener;
import elxris.Useless.Objects.Mail;
import elxris.Useless.Utils.Archivo;
import elxris.Useless.Utils.Strings;
 
public class Useless extends JavaPlugin {
    private Configuration warpcache;
    private Mail mail;
    private Archivo pin;
    private static Useless plugin;
    
    public void onEnable(){
        plugin = this;
        checkConfiguration();
        new Strings(this.getConfig());
        //Commando Tempowal Warp
        warpcache = new MemoryConfiguration();
        getCommand("tw").setExecutor(new WarpCommand(warpcache));
        //Comando Mail
        mail = new Mail();
        getCommand("mbox").setExecutor(new MailBoxCommand(mail));
        getCommand("mboxc").setExecutor(new MailBoxCreateCommand(mail));
        //Listener Mail
        new MailListener(mail);
        //Comando Compass
        pin = new Archivo("pin.yml");
        getCommand("upin").setExecutor(new CompassCommand(pin));
        // Cargar strings
    }
    public void checkConfiguration(){
        setPath("string", "%s");
        // Warps
        String[] twinfo = {"§aAyuda /tw§r",
                "§e/tw §cn§eew [minutos]§r Para crear un warp temporal personal.",
                "Recuerda que por cada minuto te cobrará %s.",
                "§e/tw §cn§eew [nombre] [minutos]§r Hacer un warp temporal que cualuiera pueda usar.",
                "§e/tw [nombre]§r Para hacer uso de un warp temporal.",
                "§e/tw§r Muestra esta ayuda, o puede ser un comando rápido para un warp temporal personal."};
        setPath("tw.info", twinfo);
        String[] s167 = {"Warp Temporal de %d minutos creado.",
        "Usa §e/tw%s§r para usarlo."};
        setPath("tw.s.created", s167);
        setPath("tw.s.remain", "Quedan §4%d§r segundos para la destrucción de el warp temporal.");
        setPath("tw.s.teleported", "Teleportado.");
        setPath("tw.s.destroyed", "Warp Destruido.");
        setPath("tw.s.noMoney", "No puedes pagarte un warp temporal en este momento.");
        setPath("tw.s.noExist", "No existe el warp. §e/tw§r Para más info.");
        setPath("tw.v.maxTime", 120);
        setPath("tw.v.minTime", 1);
        setPath("tw.v.price", 10.0);
        // Mail
        String[] s132 = {"§aAyuda /mbox§r",
                "§e/mbox . §rNumerar los correos que tienes.",
                "§e/mbox §cr§r§eead §rLeer el correo.",
                "§e/mbox §cc§r§elear §rBorrar correo.",
                "§e/mboxc §rAyuda para crear correos."};
        setPath("mbox.info", s132);
        setPath("mbox.list", "Tienes §e%d§r mensajes. §e/mbox§r Para más info.");
        String[] s235 = {"De: §a%s§r Fecha: %s",
                "§b§o%s§r",
                "§e/mbox§r muestra ayuda. §e/mboxc new %s§r para responder"};        
        setPath("mbox.mail", s235);
        setPath("mbox.timeago", "Hace ");
        setPath("mbox.readStart", "§c### Inicio Bandeja de Correo ###");
        setPath("mbox.readFinish", "§c### Fin Bandeja de Correo ###");
        setPath("mbox.listEnd", "Ya no tienes correos.");
        setPath("mbox.deleted", "Mensajes eliminados.");
        // Create mail
        String[] s3 = {"§aAyuda /mboxc§r",
                "Para crear un correo debes primero hacer un borrador para después agregar el mensaje.",
                "§e/mboxc §cn§eew [Usuario] ... §rCrea un borrador con destinatario o destinatarios. Uno mínimo.",
                "§e/mboxc §ca§edd [Mensaje] §rAgrega el mensaje a el correo.",
                "§e/mboxc §cc§elear §rBorra el mensaje.",
                "§e/mboxc §cs§eend §rYa que tienes listo el mensaje, este comando lo envía.",
                "§e/mboxc sendall §ro §e/mboxc sa§r (Solo Admins)Envía el mensaje a todos los usuarios."};
        setPath("mboxc.info", s3);
        setPath("mboxc.noPlayerAdded", "Ningún destinatario, por lo tanto mensaje no creado.");
        setPath("mboxc.playerNotExist", "El jugador %s no enontrado. No ha sido agregado.");
        setPath("mboxc.catched", "Mensaje recibido.");
        setPath("mboxc.sended", "Mensaje enviado.");
        setPath("mboxc.limit", "Lo siento, ya has superado el límite de 300 caracteres.");
        setPath("mboxc.created", "Borrador creado. §e/mboxc add <Mensaje>§r para añadir mensaje.");
        setPath("mboxc.add", "Mensaje añadido, recuerda que puedes añadir más. §e/mboxc send§r para enviar.");
        // Compass
        String[] s548 = {"§aAyuda /upin§r", 
                "§e/upin <nombre> <x> <y> <z>§r Para crear una posición que puede ser localizada con una brújula.",
                "§e/upin <nombre>§r Para usarla.",
                "§e/upin §cl§eist§r Para ver tus posiciones.",
                "§e/upin listall§r o §e/upin la§r Para ver todas las posiciones."};
        setPath("upin.info", s548);
        // Errores y alertas.
        setPath("alert.notsaved", "Error, no se ha podido guardar: ");
        setPath("alert.noEconomy", "No hay plugin de economía. Se cobrará con puntos de experiencia.");
        // Formatos
        String[] units = {"mes", "meses", "dia", "dias", "hora", "horas", "minuto", "minutos", "segundo", "segundos"};
        setPath("f.units", units);
        String[] months = {"Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"};
        setPath("f.months", months);
        // Esperiencia
        setPath("exp.format", "%d puntos de experiencia");
        setPath("exp.cobrar", "Cobrando %d puntos de experiencia.");
        // Guarda el archivo de configuración.
        this.saveConfig();
        this.reloadConfig();
    }
    private void setPath(String path, Object v){
        if(!this.getConfig().isSet(path))
            this.getConfig().set(path, v);
    }
    public static void log(String m){
        plugin.getLogger().info(m);
    }
    public static Player getPlayer(String playerName){
        return plugin.getServer().getPlayer(playerName);
    }
    public static Useless plugin(){
        return plugin;
    }
    public static String getVersion(){
        return Useless.getVersion();
    }
}