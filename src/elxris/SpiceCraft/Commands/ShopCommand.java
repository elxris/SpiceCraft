package elxris.SpiceCraft.Commands;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import elxris.SpiceCraft.SpiceCraft;
import elxris.SpiceCraft.Objects.Factory;
import elxris.SpiceCraft.Utils.Chat;
import elxris.SpiceCraft.Utils.Strings;

public class ShopCommand extends Comando implements TabCompleter{
    public Factory f;
    public ShopCommand() {
        f = new Factory();
        // Registra los eventos de plugin.
        SpiceCraft.plugin().getServer().getPluginManager().registerEvents(f, SpiceCraft.plugin());
    }
    @Override
    public boolean onCommand(CommandSender player, Command command, String label, String[] args) {
        Player p;
        if(player instanceof Player){
            p = (Player) player;
        }else{
            return true;
        }
        if(!p.hasPermission("spicecraft.shop")){
            mensaje(p, "alert.permission");
            return true;
        }
        if(args.length == 0){ // Abre la tienda.
            f.sell(p);
            mensaje(p, "shop.openInfo");
        }else
        if(args.length == 1){
            if(isCommand("comm.shop.help", args[0])){ // Vender
                mensaje(p, "shop.info", Double.toString(((double)getValue("shop.sellRate"))*100d)+"%");
                if(p.hasPermission("spicecraft.shop.master")){
                    mensaje(p, "shop.infoMaster");
                }
            }else{ // Item info.
                buscar(p, args[0], null, null, null);
            }
        }else if(args.length == 2){
            buscar(p, args[0], args[1], null, null);
        }else if(args.length == 3){
            buscar(p, args[0], args[1], args[2], null);
        }else if(args.length == 4){
            buscar(p, args[0], args[1], args[2], args[3]);
        }
        return true;
    }
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command,
            String label, String[] args) {
        if(sender instanceof Player){
            return tab((Player)sender, args);
        }
        return null;
    }
    private List<String> tab(Player p, String[] args){
        if(args.length == 1){
            return f.lookItems(args[0], true);
        }
        return null;
    }
    private void buscar(Player p, String arg1, String arg2, String arg3, String arg4){
        List<String> items = f.lookItems(arg1);
        if(items.size() == 0){
            mensaje(p, "shop.notExist");
        }else if(items.size() == 1){ // Si sólo hay un resultado.
            if(arg2 == null && arg3 == null){ // Si no hay otro argumento, muestra información.
                f.showItemInfo(p, items.get(0));
            }else if(isCommand("comm.shop.reset", arg2) && arg3 == null){ // Si hay reset, resetea el precio.
                if(p.hasPermission("spicecraft.shop.master")){
                    f.reset(items.get(0));
                    Chat.mensaje(p, "shop.resetItem", items.get(0));
                }else{
                    Chat.mensaje(p, "alert.permission");
                }
            }else if(arg2 != null && arg3 == null){ // Si hay una cantidad, compra.
                shop(p, items.get(0), arg2);
            }else if(isCommand("comm.shop.set", arg2) && arg3 != null){// Si hay un set, setea el precio.
                if(isDouble(arg3)){
                    if(p.hasPermission("spicecraft.shop.master")){
                        f.setPrice(items.get(0), Double.parseDouble(arg3));
                        Chat.mensaje(p, "shop.setPrice", items.get(0));
                    }else{
                        Chat.mensaje(p, "alert.permission");
                    }
                }
            }
        }else{ // Si hay más de un objeto en la búsqueda.
            if(arg2 != null){ // Si especifica número. Pero no es entero o se sale de rango.
                if(!isInteger(arg2)){
                    mensaje(p, "alert.noInteger");
                    return;
                }
                if(Integer.parseInt(arg2) > items.size()-1 || Integer.parseInt(arg2) < 0){
                    mensaje(p, "shop.notExist");
                    return;
                }
            }
            if(arg2 == null){ // Sólo si no especifica ni numero ni cantidad.
                for(int i = 0; i < items.size(); i++){
                    items.set(i, String.format(Strings.getString("shop.searchItem"), i, items.get(i)));
                }
                mensaje(p, "shop.searchHead", arg1);
                mensaje(p, items);
                if(items.size() == 18){
                    mensaje(p, "shop.andMore", null);
                }
            }else if(arg2 != null && arg3 == null){ // Si especifica el número.
                f.showItemInfo(p, items.get(Integer.parseInt(arg2)));
            }else{ // Si hay número y cantidad.
                if(p.hasPermission("spicecraft.shop.master")){
                    if(isCommand("comm.shop.reset", arg3)){
                        f.reset(items.get(Integer.parseInt(arg2)));
                        Chat.mensaje(p, "shop.resetItem", items.get(Integer.parseInt(arg2)));
                        return;
                    }else if(isCommand("comm.shop.set", arg3) && arg4 != null && isDouble(arg4)){
                        f.setPrice(items.get(Integer.parseInt(arg2)), Double.parseDouble(arg4));
                        Chat.mensaje(p, "shop.setPrice", items.get(Integer.parseInt(arg2)));
                        return;
                    }
                }
                if(!isInteger(arg3)){
                    mensaje(p, "alert.noInteger");
                    return;
                }
                shop(p, items.get(Integer.parseInt(arg2)), arg3);
            }
        }
    }
    private void shop(Player p, String item, String cantidad){
        if(!isInteger(cantidad)){
            mensaje(p, "alert.noInteger");
            return;
        }
        if(Integer.parseInt(cantidad) < 1){
            mensaje(p, "alert.positive");
            return;
        }
        if(f.shop(p, item, Integer.parseInt(cantidad))){
            mensaje(p, "shop.shoped");
        }
    }
}
