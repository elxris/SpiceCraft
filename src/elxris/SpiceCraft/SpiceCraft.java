package elxris.SpiceCraft;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.OfflinePlayer;
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
import elxris.SpiceCraft.Listener.MonsterListener;
import elxris.SpiceCraft.Objects.Mail;
 
public class SpiceCraft extends JavaPlugin {
    private Mail mail;
    private static SpiceCraft plugin;
    
    public void onEnable(){
        plugin = this;
        new CheckConfiguration();
        //Commando Tempowal Warp
        WarpCommand tw = new WarpCommand();
        getCommand("tw").setExecutor(tw);
        //Listener Warp
        this.getServer().getPluginManager().registerEvents(tw, this);
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
        //Comando Librer�a
        getCommand("lib").setExecutor(new LibCommand());
        //Comando Tienda
        ShopCommand shop = new ShopCommand();
        getCommand("shop").setExecutor(shop);
        getCommand("shop").setTabCompleter(shop);
        //Listener Mosnter
        this.getServer().getPluginManager().registerEvents(new MonsterListener(), this);
    }
    public static void log(String m){
        plugin.getLogger().info(m);
    }
    public static Player getOnlinePlayer(String playerName){
        return plugin().getServer().getPlayer(playerName);
    }
    public static Player getOnlineExactPlayer(String playerName){
        return plugin().getServer().getPlayerExact(playerName);
    }
    public static List<String> getOfflinePlayerNamesMatch(String player){
        String players[] = getOfflinePlayerNames();
        List<String> jugadores = new ArrayList<String>();
        for(String p : players){
            if(p.toLowerCase().contains(player.toLowerCase())){
                if(p.toLowerCase().contentEquals(player.toLowerCase())){
                    jugadores = new ArrayList<String>();
                    jugadores.add(p);
                    break;
                }else{
                    jugadores.add(p);
                }
            }
        };
        return jugadores;
    }
    public static String[] getOfflinePlayerNames(){
        OfflinePlayer players[] = plugin.getServer().getOfflinePlayers();
        String playerNames[] = new String[players.length];
        for(int i = 0; i < players.length; i++){
            playerNames[i] = players[i].getName();
        }
        return playerNames;
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