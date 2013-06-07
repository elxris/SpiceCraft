package elxris.SpiceCraft.Commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import elxris.SpiceCraft.Utils.Archivo;
import elxris.SpiceCraft.Utils.Econ;
import elxris.SpiceCraft.Utils.Strings;

public class LibCommand extends Comando{
    private Archivo file;
    private FileConfiguration cache;
    private Econ econ;
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
        if(!jugador.hasPermission("spicecraft.lib")){
            mensaje(jugador, "alert.permission");
            return true;
        }
        // Muestra la ayuda.
        if(args.length == 0){
            mensaje(jugador, "lib.info");
        }else
        // Listar, Top, paga.
        if(args.length == 1){
            if(isCommand("comm.lib.list", args[0])){
                listarLibros(jugador, 1);
            }else if(isCommand("comm.lib.top", args[0])){
                if(!getCache().isSet("top")){
                    top();
                }
                List<Integer> top = getCache().getIntegerList("top");
                List<Integer> topTmp = getCache().getIntegerList("top");
                List<String> list = Strings.getStringList("lib.top");
                int contador = 1;
                for(int k: top){
                    if(!hasBook(k)){
                        topTmp.remove((Object)k);
                        continue;
                    }
                    list.add(String.format(
                            Strings.getString("lib.topItem"),
                            contador++, item(jugador, k)));
                }
                getCache().set("top", topTmp);
                mensaje(jugador, list);
            }else if(isCommand("comm.lib.pay", args[0])){
                String list = "";
                double dinero = 0;
                for(String libro: getCache().getConfigurationSection("libro").getKeys(false)){
                    int k = Integer.parseInt(libro);
                    if(isMyBook(jugador, k)){
                        if(getRetributions(k) > 0){
                            dinero += getRetributions(k);
                            setRetributions(k);
                            list = list.concat(item(k));
                        }
                    }
                }
                if(getEcon().pagar(jugador, dinero)){
                    mensaje(jugador, "lib.pay", list, getEcon().getPrecio(dinero));
                }else{
                    mensaje(jugador, "lib.noPay");
                }
            }
        }else
        // Comprar, Info, Vender, Borrar
        if(args.length == 2){
            if(isCommand("comm.lib.buy", args[0])){
                if(!jugador.hasPermission("spicecraft.lib.buy")){
                    mensaje(jugador, "alert.permission");
                    return true;
                }
                if(!isInteger(args[1])){
                    mensaje(jugador, "alert.noInteger");
                    return true;
                }
                if(!hasBook(args[1])){
                    mensaje(jugador, "lib.noBook");
                    return true;
                }
                if(getEcon().cobrar(jugador, getCache().getDouble("libro."+args[1]+".cost"))){
                    buyBook(jugador, Integer.parseInt(args[1]));
                    mensaje(jugador, "lib.buy");
                    mensaje(getCache().getString("libro."+args[1]+".autor"), "lib.sell");
                }else{
                    mensaje(jugador, "lib.noMoney");
                }
            }else if(isCommand("comm.lib.info", args[0])){
                if(!isInteger(args[1])){
                    mensaje(jugador, "alert.noInteger");
                    return true;
                }
                if(!hasBook(args[1])){
                    mensaje(jugador, "lib.noBook");
                    return true;
                }
                // Autor, Título, Páginas, Precio, Ventas
                BookMeta book = getBookMeta(args[1]);
                mensaje(jugador, "lib.bookInfo",
                        book.getAuthor(),
                        book.getTitle(),
                        book.getPageCount(),
                        getEcon().getPrecio(getCache().getDouble("libro."+args[1]+".cost")),
                        getCache().getInt("libro."+args[1]+".count"));
            }else if(isCommand("comm.lib.sell", args[0])){
                if(!jugador.hasPermission("spicecraft.lib.sell")){
                    mensaje(jugador, "alert.permission");
                    return true;
                }
                if(!isDouble(args[1])){
                    mensaje(jugador, "alert.noInteger");
                    return true;
                }
                // Si el precio no es adecuado
                if(Double.parseDouble(args[1]) < 0){
                    mensaje(jugador, "alert.positive");
                    return true;
                }
                if(jugador.getItemInHand() == null){
                    mensaje(jugador, "lib.noHand");
                    return true;
                }
                if(jugador.getItemInHand().getType() != Material.WRITTEN_BOOK){
                    mensaje(jugador, "lib.isNotABook");
                    return true;
                }
                // Si no es el autor del libro.
                BookMeta book = ((BookMeta)jugador.getItemInHand().getItemMeta());
                if(!book.getAuthor().contentEquals(jugador.getName())){
                    mensaje(jugador, "lib.wrongAuthor");
                    return true;
                }
                // Está repetido
                if(isRepeated(book)){
                    mensaje(jugador, "lib.repeated");
                    return true;
                }
                sellBook((BookMeta)(jugador.getItemInHand().getItemMeta()), Double.parseDouble(args[1]));
                mensaje(jugador, "lib.send");
                save();
            }else if(isCommand("comm.lib.del", args[0])){
                if(!isInteger(args[1])){
                    mensaje(jugador, "alert.noInteger");
                    return true;
                }
                if(!hasBook(args[1])){
                    mensaje(jugador, "lib.noBook");
                    return true;
                }
                // Si no es el autor del libro.
                if(!getCache().getString("libro."+args[1]+".autor").contentEquals(jugador.getName())
                        && !jugador.hasPermission("spicecraft.lib.master")){
                    mensaje(jugador, "lib.wrongAuthor");                        
                    return true;
                }
                getCache().set("libro."+args[1], null);
                mensaje(jugador, "lib.del");
                save();
            }else if(isCommand("comm.lib.list", args[0])){
                if(isInteger(args[1])){
                    listarLibros(jugador, Integer.parseInt(args[1]));                    
                }else{
                    listarLibros(jugador, 1);
                }
            }
        } // Fin if
        return true;
    }
    private String item(String path, int id){
        return String.format(Strings.getString(path), id,
                getCache().getString("libro."+id+".title"),
                getCache().getString("libro."+id+".autor"),
                econ.getPrecio(getCache().getDouble("libro."+id+".cost")));
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
    private void top(){
        if(!getCache().isSet("libro")){
            return;
        }
        String[] libros = getCache().getConfigurationSection("libro").getKeys(false).toArray(new String[0]);
        if(libros.length < 1){
            return;
        }
        int[] compras = new int[libros.length];
        for(int i = 0; i < compras.length; i++){
            compras[i] = getCount(Integer.parseInt(libros[i]));
        }
        
        List<Integer> ordered = new ArrayList<>();
        List<Object> result = new ArrayList<>();
        int n = 0;
        for(int e: compras){
            int i;
            for(i = 0; i < ordered.size(); i++){
                if(ordered.get(i) < e){
                    break;
                }
            }
            ordered.add(i, e);
            result.add(i, libros[n]);
            n++;
        }
        int index = (int)getValue("lib.topSize");
        if(result.size() < index){
            index = result.size();
        }
        getCache().set("top", result.subList(0, index));
    }
    private double getRetributions(int id){
        double diff = getCache().getInt("libro."+id+".count")-getCache().getInt("libro."+id+".payed");
        double percent = (double)getValue("lib.rate");
        double price = getCache().getInt("libro."+id+".cost");
        return price*percent*diff;
    }
    private void setRetributions(int id){
        getCache().set("libro."+id+".payed", getCache().getInt("libro."+id+".count"));
    }
    private void buyBook(Player jugador, int id){
        for(ItemStack item: jugador.getInventory().addItem(getBook(id)).values()){
            jugador.getWorld().dropItemNaturally(jugador.getLocation(), item);
        }
        addCount(id, 1);
    }
    private ItemStack setBook(String autor, String title, List<String> pages){
        ItemStack book = new ItemStack(Material.WRITTEN_BOOK);
        BookMeta meta = (BookMeta) book.getItemMeta();
        meta.setAuthor(autor);
        meta.setTitle(title);
        meta.setDisplayName(title);
        meta.setPages(pages);
        book.setItemMeta(meta);
        return book;
    }
    private boolean sellBook(BookMeta book, double price){
        int id = 0;
        for(boolean n = false; !n;){
            id = new Random().nextInt(10000);
            if(!hasBook(id)){
                n = true;
            }
        }
        String path = "libro."+id;
        getCache().set(path+".autor", book.getAuthor());
        getCache().set(path+".title", book.getTitle());
        getCache().set(path+".text", book.getPages());
        getCache().set(path+".cost", price);
        getCache().set(path+".count", 0);
        getCache().set(path+".payed", 0);
        return true;
    }
    private ItemStack getBook(int id){
        return setBook(getCache().getString("libro."+id+".autor"), 
                getCache().getString("libro."+id+".title"), 
                getCache().getStringList("libro."+id+".text"));
    }
    private BookMeta getBookMeta(int id){
        return (BookMeta) getBook(id).getItemMeta();
    }
    private BookMeta getBookMeta(String id){
        return getBookMeta(Integer.parseInt(id));
    }
    private void setCount(int id, int v){
        getCache().set("libro."+id+".count", v);
        save();
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
    private boolean isRepeated(BookMeta book){
        if (!getCache().isSet("libro")) {
            return false;
        }
        for(String k: getCache().getConfigurationSection("libro").getKeys(false)){
            if(getBookMeta(k).getPages().toString().contentEquals(book.getPages().toString())){
                return true;
            }
        }
        return false;
    }
    private List<String> invertList(List<String> original){
        List<String> nueva = new ArrayList<String>();
        for(int i = original.size()-1; i >= 0; i--){
            nueva.add(original.get(i));
        }
        return nueva;
    }
    private void listarLibros(Player p, int pagina){
        List<String> lista = new ArrayList<String>();
        List<String> listaYo = new ArrayList<String>();
        if(getCache().isSet("libro")){
            for(String libro: getCache().getConfigurationSection("libro").getKeys(false)){
                int k = Integer.parseInt(libro);
                if(isMyBook(p, k)){
                    listaYo.add(itemMe(k));
                }else{
                    lista.add(item(k));
                }
            }
            listaYo = invertList(listaYo);
            lista = invertList(lista);
            listaYo.addAll(lista);
        }
        mensaje(p, paginarLista(listaYo, pagina));
    }
    private List<String> paginarLista(List<String> libros, int pagina){
        List<String> nueva = new ArrayList<String>();
        int size = libros.size()-1;
        int max = ((size)+(8-(size)%8))/8;
        size++;
        if(pagina > max){
            pagina = max;
        }
        nueva.add(String.format(Strings.getString("lib.list"), pagina, max));
        for(int i = 8*(pagina-1);i < 8*pagina && i <= size-1;i++){
            nueva.add(libros.get(i));
        }
        return nueva;
    }
    // Getters
    private void setFile(Archivo file) {
        this.file = file;
        if (!file.exist()) {
            Archivo.blankFile(file.getName());
        }
    }
    private Archivo getFile() {
        return file;
    }
    private void setCache(FileConfiguration cache) {
        this.cache = cache;
    }
    private FileConfiguration getCache() {
        if(cache == null){
            load();
        }
        if(!cache.contains("libro")) {
            cache.set("libro", null);
        }
        return cache;
    }
    private void save(){
        getCache().set("top", null);
        getFile().save(getCache());
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
