package elxris.Useless.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import elxris.Useless.Utils.Archivo;
import elxris.Useless.Utils.Chat;

public class LibCommand extends Comando{
    Archivo file;
    FileConfiguration cache;
    public LibCommand() {
        setFile(new Archivo("lib.yml"));
    }
    @Override
    public boolean onCommand(CommandSender sender, Command comando, String label,
            String[] args) {
        Player jugador;
        if(sender instanceof Player){
            jugador = (Player) sender;
        }else{
            return true;
        }
        if(!jugador.hasPermission("useless.lib")){
            return true;
        }
        // Muestra la ayuda.
        if(args.length == 0){
            Chat.mensaje(jugador, "lib.info");
        }
        // Listar, Top, paga.
        if(args.length == 1){
            
        }
        return true;
    }
    // Getters
    private void setFile(Archivo file) {
        this.file = file;
    }
    private Archivo getFile() {
        return file;
    }
    private void setCache(FileConfiguration cache) {
        this.cache = cache;
    }
    private FileConfiguration getCache() {
        return cache;
    }
    private void save(){
        getFile().save(getCache());
        load();
    }
    private void load(){
        setCache(getFile().load());
    }
}
