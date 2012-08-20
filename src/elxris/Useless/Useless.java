package elxris.Useless;

import java.io.File;
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
 
public class Useless extends JavaPlugin {
    Configuration fc, warpcache, pincache;
    Mail mail;
    Logger lggr;
    File file;
    public void onEnable(){
        fc = this.getConfig();
        lggr = this.getLogger();
        checkConfiguration();
        //Commando Tempowal Warp
        warpcache = new MemoryConfiguration();
        getCommand("tw").setExecutor(new WarpCommand(this, warpcache));
        //Comando Mail
        mail = new Mail(this);
        getCommand("mbox").setExecutor(new MailBoxCommand(this, mail));
        getCommand("mboxc").setExecutor(new MailBoxCreateCommand(this, mail));
        //Listener Mail
        new MailListener(this);
        //Comando Compass
        pincache = new MemoryConfiguration();
        getCommand("tpin").setExecutor(new CompassCommand(this, pincache));
        /*file = new File(getDataFolder(), "mail.yml");
        mailcache = YamlConfiguration.loadConfiguration(file);
        mailcache.set("prueba.pruebab.prueba", 123);
        mailcache.set("prueba.pruebab.prueba2", 123);
        mailcache.set("prueba.pruebab.prueba", null);
        mailcache.set("prueba.pruebab.prueba2", null);
        lggr.info(Boolean.toString(mailcache.isConfigurationSection("prueba.pruebab")));
        try {
            mailcache.save("mail.yml");
            mailcache.load("mail.yml");
        } catch (IOException | InvalidConfigurationException e) {
        }
        lggr.info(Boolean.toString(mailcache.isConfigurationSection("prueba.pruebab")));*/
    }

    public void checkConfiguration(){
        String path;
        path = "version";
        if(!this.getConfig().isSet(path))
            this.getConfig().set(path, Double.parseDouble(this.getDescription().getVersion()));
        // Warps
        path = "tw.info";
        if(!this.getConfig().isSet(path))
            this.getConfig().set(path, "/tw [minutos]\nRecuerda que por cada minuto te cobrar\u00E1 10 puntos de experiencia.");
        path = "tw.s.created";
        if(!this.getConfig().isSet(path))
            this.getConfig().set(path, "Warp Temporal de %d minutos, y te costo %d puntos de experiencia.");
        path = "tw.s.remain";
        if(!this.getConfig().isSet(path))
            this.getConfig().set(path, "Quedan %d segundos.");
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
        if(!this.getConfig().isSet(path))
            this.getConfig().set(path, 
                    "§l/mbox list §rListar los correos que tienes.\n" +
            		"§l/mbox next §rLeer el siguiente correo.\n" +
            		"§l/mbox keep §rConserva el ultimo correo leido.\n");
        path = "mbox.list";
        if(!this.getConfig().isSet(path))
            this.getConfig().set(path, "Tienes (%d) mensajes.\n§l/mbox§r Ver la ayuda.");
        path = "mbox.mail";
        if(!this.getConfig().isSet(path))
            this.getConfig().set(path, "De: %s\nFecha: %s\n%s\n§l/mbox§r Ver la ayuda. §l/mboxc reply§r Responder");
        path = "mbox.listEnd";
        if(!this.getConfig().isSet(path))
            this.getConfig().set(path, "Ya no tienes más correos.");
        // TODO Mail Conservado
        // Create mail
        path = "mboxc.info";
        if(!this.getConfig().isSet(path))
            this.getConfig().set(path, 
                    "Para crear un correo debes primero hacer Reply o Create para después agregar el mensaje.\n" +
                    "§l/mboxc Reply §rResponde al usuario del último mensaje leido. " +
                    "Al usar el comando se crea un borrador con destinatario igual al remitente.\n" +
                    "§l/mboxc Create [Usuario] ... §rCrea un borrador con destinatario o destinatarios. Uno mínimo.\n" +
                    "§l/mboxc Add [Mensaje]§rAgrega el mensaje a el correo.\n" +
                    "§l/mboxc Clear§rBorra el mensaje." +
                    "§l/mboxc Send§rYa que tienes listo el mensaje, este comando lo envía.\n" +
                    "§l/mboxc SendAll§r (Solo Admins)Envía el mensaje a todos los usuarios que hayan entrado al servidor.");
        // TODO Primero crear el correo y despues el mensaje.
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
        
        // Compass
        
        this.saveConfig();
    }
}