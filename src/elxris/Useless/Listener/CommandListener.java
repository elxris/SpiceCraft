package elxris.Useless.Listener;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import elxris.Useless.Useless;
import elxris.Useless.Utils.Archivo;
import elxris.Useless.Utils.Chat;

public class CommandListener implements Listener{
    private Archivo file;
    private FileConfiguration fc;
    public CommandListener() {
        load("commands.yml");
        init();
    }
    // Inicia y prepara la configuración.
    private void init(){
        fc = new Archivo("commands.yml").load();
    }
    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent event){
        if(event.getPlayer() != null){
            if(event.getPlayer().hasPermission("useless.cmd")){
                if(getCommand(event.getPlayer(), event.getMessage())){
                    event.setCancelled(true);
                }
            }
        }
    }
    // Muestra un comando
    private boolean getCommand(Player p, String command){
        String[] s = command.split(" ");
        command = command.replace(' ', '.');
        command = command.substring(1);
        if(s[0].contains("/root")){
            return false;
        }
        if(s[0].contains("/uselessreload")){
            if(p.hasPermission("useless.cmd.reload")){
                Useless.reload();
                reload();
                Chat.mensaje((Player) p, "cmd.reload");
                return true;
            }
        }
        if(s.length > 0){
            if(isSet(command+".*")){
                return mensaje(p, command+".*");               
            }else if(isItBook(command)){
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
    private boolean isItBook(String path){
        if(isSet(path+".!")){
            return true;
        }
        return false;
    }
    private boolean mensaje(Player p, String path) {
        Chat.mensaje(p, getFc().getStringList(path));
        return true;
    }
    private ItemStack makeBook(String path){
        ItemStack book = new ItemStack(Material.WRITTEN_BOOK);
        BookMeta meta = (BookMeta) book.getItemMeta();
        List<String> lineas = getFc().getStringList(path+".!");
        meta.setTitle(lineas.get(0));
        meta.setAuthor(lineas.get(1));
        lineas = lineas.subList(2, lineas.size());
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
