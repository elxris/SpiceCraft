package elxris.Useless;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import elxris.Useless.Commands.CompassCommand;
import elxris.Useless.Commands.LibCommand;
import elxris.Useless.Commands.MailBoxCommand;
import elxris.Useless.Commands.MailBoxCreateCommand;
import elxris.Useless.Commands.WarpCommand;
import elxris.Useless.Listener.CommandListener;
import elxris.Useless.Listener.CompassListener;
import elxris.Useless.Listener.MailListener;
import elxris.Useless.Objects.Mail;
import elxris.Useless.Utils.Strings;
 
public class Useless extends JavaPlugin {
    private Mail mail;
    private static Useless plugin;
    
    public void onEnable(){
        plugin = this;
        new CheckConfiguration();
        new Strings(this.getConfig());
        //Commando Tempowal Warp
        getCommand("tw").setExecutor(new WarpCommand());
        //Comando Mail
        mail = new Mail();
        getCommand("mbox").setExecutor(new MailBoxCommand(mail));
        getCommand("mboxc").setExecutor(new MailBoxCreateCommand(mail));
        //Listener Mail
        this.getServer().getPluginManager().registerEvents(new MailListener(mail), this);
        //Listener Command
        this.getServer().getPluginManager().registerEvents(new CommandListener(), this);
        //Listiener Compass
        this.getServer().getPluginManager().registerEvents(new CompassListener(), this);
        //Comando Compass
        getCommand("upin").setExecutor(new CompassCommand());
        //Comando Librer�a
        getCommand("lib").setExecutor(new LibCommand());
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