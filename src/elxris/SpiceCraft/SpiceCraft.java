package elxris.SpiceCraft;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.TreeMap;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import elxris.SpiceCraft.Commands.CompassCommand;
import elxris.SpiceCraft.Commands.LibCommand;
import elxris.SpiceCraft.Commands.MailBoxCommand;
import elxris.SpiceCraft.Commands.ReloadCommand;
import elxris.SpiceCraft.Commands.ShopCommand;
import elxris.SpiceCraft.Commands.WarpCommand;
import elxris.SpiceCraft.Listener.CommandListener;
import elxris.SpiceCraft.Listener.MailListener;
import elxris.SpiceCraft.Listener.MonsterListener;
import elxris.SpiceCraft.Objects.Mail;
 
public class SpiceCraft extends JavaPlugin {
    private Mail mail;
    private static SpiceCraft plugin;
    private static TreeMap<String, String> offlinePlayers;
    private static OfflinePlayer[] offlinePlayersArray;
    
    public void onEnable(){
        plugin = this;
        init();
    }
    public void init(){
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
        //this.getServer().getPluginManager().registerEvents(new CompassListener(), this);
        //Comando Compass
        getCommand("upin").setExecutor(new CompassCommand());
        //Comando Librería
        getCommand("lib").setExecutor(new LibCommand());
        //Comando Tienda
        ShopCommand shop = new ShopCommand();
        getCommand("shop").setExecutor(shop);
        getCommand("shop").setTabCompleter(shop);
        //Listener Mosnter
        this.getServer().getPluginManager().registerEvents(new MonsterListener(), this);
        // Reload
        getCommand("/spicecraft").setExecutor(new ReloadCommand());
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
    	return getOfflinePlayerNamesMatch(player, 5);
    }
    public static List<String> getOfflinePlayerNamesMatch(String player, int limit){
        String playerLC = player.toLowerCase();
        TreeMap<String, String> players = getOfflinePlayerNames();
        Collection<String> busqueda;
        busqueda = players.subMap(playerLC, true, playerLC+"z", true).values();
        List<String> jugadores = new ArrayList<String>(busqueda.size());
        jugadores.addAll(busqueda);
        jugadores = jugadores.subList(0, limit);
        return jugadores;
    }
    public static TreeMap<String, String> getOfflinePlayerNames(){
        OfflinePlayer players[] = plugin.getServer().getOfflinePlayers();
        if (offlinePlayersArray == null || offlinePlayersArray != players) {
            offlinePlayersArray = players;
            TreeMap<String, String> playerNames = new TreeMap<String, String>();
            for(int i = 0; i < players.length; i++){
                String name = players[i].getName();
                playerNames.put(name.toLowerCase(), name);
            }
            offlinePlayers = playerNames;
        }
        return offlinePlayers;
    }
    public static SpiceCraft plugin(){
        return plugin;
    }
    public static String getVersion(){
        return plugin.getDescription().getVersion();
    }
}