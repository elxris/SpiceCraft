package elxris.SpiceCraft;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import elxris.SpiceCraft.Commands.CompassCommand;
import elxris.SpiceCraft.Commands.LibCommand;
import elxris.SpiceCraft.Commands.MailBoxCommand;
import elxris.SpiceCraft.Commands.ShopCommand;
import elxris.SpiceCraft.Commands.WarpCommand;
import elxris.SpiceCraft.Listener.CommandListener;
import elxris.SpiceCraft.Listener.CompassListener;
import elxris.SpiceCraft.Listener.MailListener;
import elxris.SpiceCraft.Objects.Mail;
 
public class SpiceCraft extends JavaPlugin {
    private Mail mail;
    private static SpiceCraft plugin;
    
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
    }
    public static void log(String m){
        plugin.getLogger().info(m);
    }
    public static Player getPlayer(String playerName){
        return plugin.getServer().getPlayer(playerName);
    }
    public static SpiceCraft plugin(){
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