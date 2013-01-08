package elxris.Useless.Commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import elxris.Useless.Utils.Archivo;
import elxris.Useless.Utils.Chat;
import elxris.Useless.Utils.Strings;
import elxris.Useless.Utils.Econ;

public class LibCommand extends Comando{
    Archivo file;
    FileConfiguration cache;
    Econ econ;
    public LibCommand() {
        setFile(new Archivo("lib.yml"));
        setEcon(new Econ());
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
            if(isCommand("comm.lib.list", args[0])){
                List<Integer> libros = getCache().getIntegerList("libros");
                String lista = "";
                String listaYo = "";
                for(int k: libros){
                    if(isMyBook(jugador, k)){
                        listaYo = listaYo.concat(itemMe(k));
                    }else{
                        lista = lista.concat(item(k));
                    }
                }
                Chat.mensaje(jugador, "lib.list", listaYo+lista);
            }else if(isCommand("comm.lib.top", args[0])){
                List<Integer> top = getCache().getIntegerList("top");
                String list = "";
                int contador = 1;
                for(int k: top){
                    list = list.concat(String.format(
                            Strings.getString("lib.topItem"),
                            contador, item(jugador, k)));
                }
                Chat.mensaje(jugador, "lib.top", list);
            }else if(isCommand("comm.lib.pay", args[0])){
                List<Integer> libros = getCache().getIntegerList("libros");
                String list = "";
                double dinero = 0;
                for(int k: libros){
                    if(isMyBook(jugador, k)){
                        if(getRetributions(k) > 0){
                            dinero += getRetributions(k);
                            setRetributions(k);
                            list.concat(item(k));
                        }
                    }
                }
                if(getEcon().pagar(jugador, dinero)){
                    String.format(Strings.getString("lib.pay"));
                }
            }
        }
        // Comprar, Info, Vender, Borrar
        if(isCommand("comm.lib.buy", args[0])){
            if(!jugador.hasPermission("useless.lib.buy")){
                Chat.mensaje(jugador, "alert.permission");
                return true;
            }
            if(!hasBook(args[0])){
                Chat.mensaje(jugador, "lib.noBook");
                return true;
            }
            if(getEcon().cobrar(jugador, getCache().getDouble("libro."+args[0]+".cost"))){
                buyBook(jugador, Integer.parseInt(args[0]));
                Chat.mensaje(jugador, "lib.buy");
                Chat.mensaje(getCache().getString("libro."+args[0]+".autor"), "lib.sell");
            }
        }else if(isCommand("comm.lib.info", args[0])){
            if(!hasBook(args[0])){
                Chat.mensaje(jugador, "lib.noBook");
                return true;
            }
            
        }
        return true;
    }
    private String item(String path, int id){
        return String.format(Strings.getString(path), id,
                Strings.getString("libro."+id+".title"),
                Strings.getString("libro."+id+".autor"));
    }
    private String item(int id){
        return item("lib.item", id);
    }
    private String itemMe(int id){
        return item("lib.itemMe", id);
    }
    private String item(Player jugador, int id){
        if(isMyBook(jugador, id)){
            return itemMe(id);
        }
        return item(id);
    }
    private boolean isMyBook(Player jugador, int id){
        if(getCache().getString("libro."+id+".autor").contentEquals(jugador.getName())){
            return true;
        }
        return false;
    }
    private void top(int id, int ventas){
        List<Integer> top = getCache().getIntegerList("top");
        boolean save = false;
        for(int i = 0; i < top.size(); i++){
            // Si las ventas del top son más pequeñas.
            if(getCache().getInt("libro."+top.get(i)+".count") < ventas){
                int idTemp;
                // Guarda temporalmente el id del top.
                idTemp = top.get(i);
                // Sobrescribe el id
                top.set(i, id);
                // El id cambia.
                id = idTemp;
                // Cambia las ventas, para hacer en cadena los cambios.
                ventas = getCache().getInt("libro."+id+".count");
                save = true;
            }
        }
        //Si no ha alacanzado el tamaño, y si es de más de un elemento, y cambió.
        if(top.size() < Strings.getInt("lib.topSize") && top.size() > 1 && save){
            top.add(id);
        }
        if(save){
            getCache().set("top", top);
            save();
        }
    }
    private double getRetributions(int id){
        double diff = getCache().getInt("libro."+id+".count")-getCache().getInt("libro."+id+".payed");
        double percent = Strings.getInt("lib.percent");
        double price = getCache().getInt("libro."+id+".cost");
        return price*(percent/100.0)*diff;
    }
    private void setRetributions(int id){
        getCache().set("libro."+id+".count", getCache().getInt("libro."+id+".count"));
    }
    private void buyBook(Player jugador, int id){
        jugador.getInventory().addItem(getBook(id));
        addCount(id, 1);
    }
    private ItemStack getBook(int id){
        ItemStack book = new ItemStack(Material.WRITTEN_BOOK);
        BookMeta meta = (BookMeta) book.getItemMeta();
        meta.setAuthor(getCache().getString("libro."+id+".autor"));
        meta.setPages(getCache().getStringList("libro."+id+".text"));
        meta.setTitle(getCache().getString("libro."+id+".title"));
        return book;
    }
    private void setCount(int id, int v){
        getCache().set("libro."+id+".count", v);
    }
    private int getCount(int id){
        return getCache().getInt("libro."+id+".count");
    }
    private void addCount(int id, int v){
        setCount(id, getCount(id)+v);
    }
    private boolean hasBook(int id){
        return getCache().isSet("libro."+id+".autor");
    }
    private boolean hasBook(String id){
        return hasBook(Integer.parseInt(id));
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
    public void setEcon(Econ econ) {
        this.econ = econ;
    }
    public Econ getEcon() {
        return econ;
    }
}
