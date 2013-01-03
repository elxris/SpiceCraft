package elxris.Useless;

import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.MemoryConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import elxris.Useless.Commands.CompassCommand;
import elxris.Useless.Commands.MailBoxCommand;
import elxris.Useless.Commands.MailBoxCreateCommand;
import elxris.Useless.Commands.WarpCommand;
import elxris.Useless.Listener.CommandListener;
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
        new CheckConfiguration();
        new Strings(this.getConfig());
        //Commando Tempowal Warp
        warpcache = new MemoryConfiguration();
        getCommand("tw").setExecutor(new WarpCommand(warpcache));
        //Comando Mail
        mail = new Mail();
        getCommand("mbox").setExecutor(new MailBoxCommand(mail));
        getCommand("mboxc").setExecutor(new MailBoxCreateCommand(mail));
        //Listener Mail
        this.getServer().getPluginManager().registerEvents(new MailListener(mail), this);
        //Listener Help
        this.getServer().getPluginManager().registerEvents(new CommandListener(), this);
        //Comando Compass
        pin = new Archivo("pin.yml");
        getCommand("upin").setExecutor(new CompassCommand(pin));
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
    public static void reload(){
        plugin.reloadConfig();
        new CheckConfiguration();
    }
}