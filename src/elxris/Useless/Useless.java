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
import elxris.Useless.Utils.Chat;
import elxris.Useless.Utils.Strings;
 
public class Useless extends JavaPlugin {
    private Configuration warpcache;
    private Mail mail;
    private Chat chat;
    private Archivo pin;
    private static Useless plugin;
    
    public void onEnable(){
        plugin = this;
        checkConfiguration();
        chat = new Chat();
        //Commando Tempowal Warp
        warpcache = new MemoryConfiguration();
        getCommand("tw").setExecutor(new WarpCommand(chat, this, warpcache));
        //Comando Mail
        mail = new Mail();
        getCommand("mbox").setExecutor(new MailBoxCommand(chat, mail));
        getCommand("mboxc").setExecutor(new MailBoxCreateCommand(chat, mail));
        //Listener Mail
        new MailListener(this, mail);
        //Comando Compass
        pin = new Archivo("pin.yml");
        getCommand("upin").setExecutor(new CompassCommand(chat, pin));
    }
    public void checkConfiguration(){
        setPath("version", this.getDescription().getVersion());
        setPath("string", "%s");
        // Warps
        setPath("tw.info", "/tw [minutos] Recuerda que por cada minuto te cobrará %d puntos de experiencia.");
        String[] s167 = {"Warp Temporal de %d minutos.",
        "Usa §l/tw§r para usarlo."};
        setPath("tw.s.created", s167);
        setPath("tw.s.remain", "Quedan §4%d§r segundos para la destrucción de el warp temporal.");
        setPath("tw.s.teleported", "Teleportado.");
        setPath("tw.s.destroyed", "Warp Destruido.");
        setPath("tw.s.noMoney", "No puedes pagarte un warp temporal en este momento.");
        setPath("tw.v.maxTime", 120);
        setPath("tw.v.minTime", 1);
        setPath("tw.v.price", 10);
        // Mail
        String[] s132 = {"§aAyuda /mbox§r",
                "§l/mbox . §rListar los correos que tienes.",
                "§l/mbox §cR§r§lead §rLeer el correo.",
                "§l/mbox §cC§r§llear §rBorrar correo.",
                "§l/mboxc §rAyuda para crear correos."};
        setPath("mbox.info", s132);
        setPath("mbox.list", "Tienes §e%d§r mensajes. §e/mbox§r Para más info.");
        String[] s235 = {"De: %s Fecha: %s",
                "§l%s§r",
                "§e§l/mbox§r Ayuda. §e§l/mboxc create %s§r Responder"};
        setPath("mbox.mail", s235);
        setPath("mbox.listEnd", "Ya no tienes correos.");
        setPath("mbox.deleted", "Mensajes eliminados.");
        // Create mail
        String[] s3 = {"§aAyuda /mboxc§r",
                "Para crear un correo debes primero hacer un §l/mboxc create§r para después agregar el mensaje.",
                "Al usar el comando se crea un borrador con destinatario igual al remitente.",
                "§l/mboxc Create [Usuario] ... §rCrea un borrador con destinatario o destinatarios. Uno mínimo.",
                "§l/mboxc Add [Mensaje] §rAgrega el mensaje a el correo.",
                "§l/mboxc Clear §rBorra el mensaje.",
                "§l/mboxc Send §rYa que tienes listo el mensaje, este comando lo envía.",
                "§l/mboxc SendAll §r(Solo Admins)Envía el mensaje a todos los usuarios."};
        setPath("mboxc.info", s3);
        setPath("mboxc.noPlayerAdded", "Ningún destinatario, por lo tanto mensaje no creado.");
        setPath("mboxc.playerNotExist", "El jugador %s no existe. No ha sido agregado.");
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
        // Errores
        setPath("alert.notsaved", "Error, no se ha podido guardar: ");
        // Formatos
        String[] units = {"mes", "meses", "dia", "dias", "hora", "horas", "minuto", "minutos", "segundo", "segundos"};
        setPath("f.units", units);
        String[] months = {"Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"};
        setPath("f.months", months);
        // Esperiencia
        setPath("exp.cobrar", "Cobrando %d puntos de experiencia.");
        // Guarda el archivo de configuración.
        this.saveConfig();
        // Carga los strings.
        new Strings(this.getConfig());
    }
    private void setPath(String path, Object v){
        if(!this.getConfig().isSet(path))
            this.getConfig().set(path, v);
    }
    public Chat getChat() {
        return chat;
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
}