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
        //Comandos tw
        setPath("comm.tw.new", "nuevo", "n");
        // Comandos mbox
        setPath("comm.mbox.count", ".");
        setPath("comm.mbox.read", "leer", "l", "read", "r");
        setPath("comm.mbox.clear", "borrar", "b", "clear", "c");
        // Comandos mboxc
        setPath("comm.mboxc.new", "nuevo", "n", "crear", "c", "new");
        setPath("comm.mboxc.add", "agregar", "añadir", "a", "add");
        setPath("comm.mboxc.send", "enviar", "e", "send", "s");
        setPath("comm.mboxc.sendall", "atodos", "t", "sendall", "sa");
        setPath("comm.mboxc.clear", "limpiar", "l", "clear", "cl");
        // Comandos upin
        setPath("comm.upin.del", "delete", "del", "delete", "borrar", "b");
        setPath("comm.upin.list", "listar", "l", "list");
        // Warps
        setPath("tw.info", "§aAyuda /tw",
                "§e/tw §cn§euevo [minutos]§r Para crear un warp temporal personal.",
                "Recuerda que por cada minuto te cobrará %s.",
                "§e/tw §cn§euevo [nombre] [minutos]§r Hacer un warp temporal que cualuiera pueda usar.",
                "§e/tw [nombre]§r Para hacer uso de un warp temporal.",
                "§e/tw§r Muestra esta ayuda, o puede ser un comando rápido para un warp temporal personal.");
        setPath("tw.s.created", "Warp Temporal de %d minutos creado.",
        "Usa §e/tw %s§r para usarlo.");
        setPath("tw.s.remain", "Quedan §4%d§r segundos para la destrucción de el warp temporal.");
        setPath("tw.s.teleported", "Teleportando..");
        setPath("tw.s.destroyed", "Warp Destruido.");
        setPath("tw.s.noMoney", "No puedes pagarte un warp temporal en este momento.");
        setPath("tw.s.noExist", "No existe el warp. §e/tw§r Para más info.");
        setPath("tw.s.exist", "Ya existe ese warp.");
        setPath("tw.s.timeLimit", "No debe ser menor a %d minutos ni mayor a %d minutos.");
        setPath("tw.s.warpLimit", "No puedes hacer otro warp, ya tienes muchos.");
        setPath("tw.v.maxTime", 120);
        setPath("tw.v.minTime", 1);
        setPath("tw.v.maxPerUser", 2);
        setPath("tw.v.price", 10.0);
        // Mail
        setPath("mbox.info", "§aAyuda /mbox",
                "§e/mbox . §rNumerar los correos que tienes.",
                "§e/mbox §cl§eeer §rLeer el correo.",
                "§e/mbox §cb§eorrar §rBorrar correo.",
                "§e/mboxc §rAyuda para crear correos.");
        setPath("mbox.list", "Tienes §e%d§r mensajes. §e/mbox§r Para más info.");
        setPath("mbox.mail", "De: §a%s§r Fecha: %s",
                "§b§o%s§r",
                "§e/mbox§r muestra ayuda. §e/mboxc §cn§euevo %s§r para responder");
        setPath("mbox.timeago", "Hace ");
        setPath("mbox.readStart", "§c### Inicio Bandeja de Correo ###");
        setPath("mbox.readFinish", "§c### Fin Bandeja de Correo ###");
        setPath("mbox.listEnd", "Ya no tienes correos.");
        setPath("mbox.deleted", "Mensajes eliminados.");
        // Create mail
        setPath("mboxc.info", "§aAyuda /mboxc",
                "Para crear un correo debes primero hacer un borrador para después agregar el mensaje.",
                "§e/mboxc §cn§eevo [Usuario] ... §rCrea un borrador con destinatario o destinatarios. Uno mínimo.",
                "§e/mboxc §ca§egregar [Mensaje] §rAgrega el mensaje a el correo.",
                "§e/mboxc §cb§eorrar §rBorra el mensaje.",
                "§e/mboxc §ce§eviar §rYa que tienes listo el mensaje, este comando lo envía.",
                "§e/mboxc a§ct§eodos§r (Solo Admins)Envía el mensaje a todos los usuarios.");
        setPath("mboxc.noPlayerAdded", "Ningún destinatario, por lo tanto mensaje no creado.");
        setPath("mboxc.playerNotExist", "El jugador %s no enontrado. No ha sido agregado.");
        setPath("mboxc.catched", "Mensaje recibido.");
        setPath("mboxc.sended", "Mensaje enviado.");
        setPath("mboxc.limit", "Lo siento, ya has superado el límite de 300 caracteres.");
        setPath("mboxc.created", "Borrador creado. §e/mboxc §ca§egregar <Mensaje>§r para añadir mensaje.");
        setPath("mboxc.add", "Mensaje añadido, recuerda que puedes añadir más. §e/mboxc §ce§enviar§r para enviar.");
        setPath("mboxc.noMessage", "§cPrimero debes crear un borrador. §e/mboxc para más ayuda.");
        setPath("mboxc.v.maxChar", 200);
        // Compass
        setPath("upin.info", "§aAyuda /upin", 
                "§e/upin [nombre] [x] [z]§r Para crear una posición que puede ser localizada con una brújula.",
                "§e/upin [nombre]§r Para usarla.",
                "§e/upin §cb§eorrar [nombre]§r Para borrarla si eres el dueño.",
                "§e/upin §cl§eistar§r Para ver todas las posiciones.");
        setPath("upin.noPin", "No existe la posicion.");
        setPath("upin.exist", "Ya existe esa posicion. Elige otra.");
        setPath("upin.list", "§a###Lista de las posiciones ###",
                "§b%s");
        setPath("upin.item", "<%s> ");
        setPath("upin.noList", "§cNo hay posiciones. Aún.");
        setPath("upin.created", "Posición creada, úsala con §e/upin %s");
        setPath("upin.set", "La posición ahora se mostrará en un compás.");
        setPath("upin.nether", "§cDe nada sirve crear una posición aquí. No se mostrará.");
        setPath("upin.del", "Borrada la posición.");
        // Errores y alertas.
        setPath("alert.notsaved", "Error, no se ha podido guardar: ");
        setPath("alert.noEconomy", "No hay plugin de economía. Se cobrará con puntos de experiencia.");
        setPath("alert.error", "§cError al usar el comando. Revisa la ayuda.");
        setPath("alert.noInteger", "Tienes que ingresar valor/es enteros.");
        // Formatos
        setPath("f.units", "mes", "meses", "dia", "dias", "hora", "horas", "minuto", "minutos", "segundo", "segundos");
        setPath("f.months", "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", 
                "Octubre", "Noviembre", "Diciembre");
        // Esperiencia
        setPath("exp.format", "%d puntos de experiencia");
        // Economía.
        setPath("econ.cobrar", "Cobrando %s.");
        // Guarda el archivo de configuración.
        this.saveConfig();
        this.reloadConfig();
    }
    private void setPath(String path, Object... v){
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