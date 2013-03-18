package elxris.Useless;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import elxris.Useless.Commands.CompassCommand;
import elxris.Useless.Commands.JobsCommand;
import elxris.Useless.Commands.LibCommand;
import elxris.Useless.Commands.MailBoxCommand;
import elxris.Useless.Commands.ShopCommand;
import elxris.Useless.Commands.WarpCommand;
import elxris.Useless.Listener.CommandListener;
import elxris.Useless.Listener.CompassListener;
import elxris.Useless.Listener.JobsListener;
import elxris.Useless.Listener.MailListener;
import elxris.Useless.Objects.Mail;
 
public class Useless extends JavaPlugin {
    private Mail mail;
    private static Useless plugin;
    
    public void onEnable(){
        plugin = this;
        new CheckConfiguration();
        //Commando Tempowal Warp
        getCommand("tw").setExecutor(new WarpCommand());
        //Comando Mail
        mail = new Mail();
        getCommand("mbox").setExecutor(new MailBoxCommand(mail));
        //Listener Mail
        this.getServer().getPluginManager().registerEvents(new MailListener(mail), this);
        //Listener Command
        this.getServer().getPluginManager().registerEvents(new CommandListener(), this);
        //Listiener Compass
        this.getServer().getPluginManager().registerEvents(new CompassListener(), this);
        //Comando Compass
        getCommand("upin").setExecutor(new CompassCommand());
        //Comando Librería
        getCommand("lib").setExecutor(new LibCommand());
        //Comando Tienda
        ShopCommand shop = new ShopCommand();
        getCommand("shop").setExecutor(shop);
        getCommand("shop").setTabCompleter(shop);
        //Jobs Listener
        this.getServer().getPluginManager().registerEvents(new JobsListener(), this);
        getCommand("jbs").setExecutor(new JobsCommand());
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
        return plugin.getDescription().getVersion();
    }
    public static void reload(){
        plugin.reloadConfig();
        new CheckConfiguration();
    }
}