package elxris.SpiceCraft.Commands;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import elxris.SpiceCraft.SpiceCraft;
import elxris.SpiceCraft.Objects.Factory;
import elxris.SpiceCraft.Utils.Econ;
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
        if(args.length == 0){ // Info
            mensaje(p, "shop.info", (double)getValue("shop.sellRate")*100+"%");
        }else
        if(args.length == 1){
            if(isCommand("comm.shop.sell", args[0])){ // Vender
                f.sell(p);
            }else{ // Item info.
                buscar(p, args[0], null, null);
            }
        }else if(args.length == 2){
            buscar(p, args[0], args[1], null);
        }else if(args.length == 3){
            buscar(p, args[0], args[1], args[2]);
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
    private void buscar(Player p, String arg1, String arg2, String arg3){
        List<String> items = f.lookItems(arg1);
        if(items.size() == 0){
            mensaje(p, "shop.notExist");
        }else if(items.size() == 1){ // Si sólo hay un resultado.
            if(arg2 != null){ // Si hay una cantidad, compra.
                shop(p, items.get(0), arg2);
            }else{ // Si no lo hay, muestra información.
                showItemInfo(p, items.get(0));
            }
        }else{ // Si hay más de un objeto en la búsqueda.
            if(arg2 != null){
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
            }else if(arg2 != null && arg3 == null){ // Si especifica el número.
                showItemInfo(p, items.get(Integer.parseInt(arg2)));
            }else{ // Si hay número y cantidad.
                if(!isInteger(arg3)){
                    mensaje(p, "alert.noInteger");
                    return;
                }
                shop(p, items.get(Integer.parseInt(arg2)), arg3);
            }
        }
    }
    private void shop(Player p, String item, String cantidad){
        if(!isInteger(cantidad) && Integer.parseInt(cantidad) > 0){
            mensaje(p, "alert.noInteger");
            return;
        }
        if(f.shop(p, item, Integer.parseInt(cantidad))){
            mensaje(p, "shop.shoped");            
        }
    }
    private void showItemInfo(Player p, String itemName){
        String item = f.searchItem(itemName);
        String id = f.getId(item)+"";
        if(f.haveData(item)){
            id = id.concat(":"+f.getData(item));
        }
        mensaje(p, "shop.itemInfo", itemName, new Econ().getPrecio(f.getPrecio(item)),
                f.getVel(item), id);
    }
}
