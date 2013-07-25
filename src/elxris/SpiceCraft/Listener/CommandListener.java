package elxris.SpiceCraft.Listener;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import elxris.SpiceCraft.SpiceCraft;
import elxris.SpiceCraft.Utils.Archivo;
import elxris.SpiceCraft.Utils.Chat;

public class CommandListener implements Listener{
    private Archivo file;
    private FileConfiguration fc;
    private List<String> loginBooks;
    public CommandListener() {
        load("commands.yml");
        init();
    }
    // Inicia y prepara la configuración.
    private void init(){
        fc = new Archivo("commands.yml").load();
        loginBooks = new ArrayList<String>();
        for(String k: fc.getKeys(true)){
            // Si es un libro.
            if(k.substring(k.length()-2).contentEquals(("!*"))){
                loginBooks.add("/"+k.substring(0, k.length()-2));
            }
        }
    }
    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent event){
        if(event.getPlayer() != null){
            if(event.getPlayer().hasPermission("spicecraft.cmd")){
                if(getCommand(event.getPlayer(), event.getMessage())){
                    event.setCancelled(true);
                }
            }
        }
    }
    @EventHandler
    public void onNewLogin(PlayerLoginEvent event){
        Player p = event.getPlayer();
        if(!p.hasPlayedBefore()){
            // Si jugó antes.
            for(String s : loginBooks){
                getCommand(p, s);
            }
        }
    }
    // Muestra un comando
    private boolean getCommand(Player p, String command){
        command = command.toLowerCase();
        String[] s = command.split(" ");
        command = command.replace(' ', '.');
        command = command.substring(1);
        if(s[0].contains("/root")){
            return false;
        }
        if(s[0].contains("/spicecraftreload")){
            if(p.hasPermission("spicecraft.cmd.reload")){
                SpiceCraft.reload();
                reload();
                Chat.mensaje((Player) p, "cmd.reload");
                return true;
            }
        }
        if(s.length > 0){
            if(isSet(command+".*")){
                return mensaje(p, command+".*");
            }else if(getBookPath(command) != null){
                Chat.mensaje(p.getName(), getFc().getStringList(getBookPath(command)).get(0));
                p.getInventory().addItem(makeBook(command));
                return true;
            }
        }
        return false;
    }
    private boolean isSet(String path){
        if(getFc().isSet(path) && getFc().isList(path)){
            return true;
        }
        return false;
    }
    private String getBookPath(String path){
        if(isSet(path+".!")){
            return path + ".!";
        }else if(isSet(path+".!*")){
            return path + ".!*";
        }
        return null;
    }
    private boolean mensaje(Player p, String path) {
        Chat.mensaje(p, getFc().getStringList(path));
        return true;
    }
    private ItemStack makeBook(String path){
        ItemStack book = new ItemStack(Material.WRITTEN_BOOK);
        BookMeta meta = (BookMeta) book.getItemMeta();
        List<String> lineas = getFc().getStringList(getBookPath(path));
        meta.setTitle(lineas.get(1));
        meta.setAuthor(lineas.get(2));
        lineas = lineas.subList(3, lineas.size());
        List<String> paginas = new ArrayList<String>();
        String pagina = new String();
        int linea = 0, caracter = 0;
        for(String i: lineas){
            if(linea >= 14 || i.length() == 0){
                paginas.add(pagina);
                pagina = new String();
                caracter = 0;
                linea = 0;
                if(i.length() == 0){
                    continue;
                }
            }
            String palabras[] = i.split("\\s");
            for(int e = 0; e < palabras.length; e++){
                if(caracter + palabras[e].length() > 230){
                    paginas.add(pagina);
                    pagina = new String();
                    caracter = 0;
                    linea = 0;
                }
                pagina += " "+palabras[e];
                caracter += palabras[e].length()+1;
            }
            pagina += "\n";
            caracter += 4;
            linea++;
        }
        paginas.add(pagina);
        meta.setPages(paginas);
        book.setItemMeta(meta);
        return book;
    }
    private void load(String name){
        setFile(new Archivo(name));
        setFc(getFile().load());
    }
    private void reload(){
        setFc(getFile().load());
    }
    //Getters y Setters
    private Archivo getFile() {
        return file;
    }
    private void setFile(Archivo file) {
        this.file = file;
    }
    private FileConfiguration getFc() {
        return fc;
    }
    private void setFc(FileConfiguration fc) {
        this.fc = fc;
    }
}
