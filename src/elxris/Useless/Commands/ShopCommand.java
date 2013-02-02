package elxris.Useless.Commands;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import elxris.Useless.Useless;
import elxris.Useless.Objects.Factory;
import elxris.Useless.Utils.Econ;
import elxris.Useless.Utils.Strings;

public class ShopCommand extends Comando{
    public Factory f;
    public ShopCommand() {
        f = new Factory();
        // Registra los eventos de plugin.
        Useless.plugin().getServer().getPluginManager().registerEvents(f, Useless.plugin());
    }
    @Override
    public boolean onCommand(CommandSender player, Command command, String label, String[] args) {
        if(!(player instanceof Player)){
            return false;
        }
        Player p = (Player)player;
        if(args.length == 0){ // Info
            mensaje(p, "shop.info", Strings.getDouble("shop.sellRate")*100+"%");
        }else
        if(args.length == 1){
            if(isCommand("comm.shop.sell", args[0])){ // Vender
                f.sell(p);
            }else{ // Item info.
                String item = f.searchItem(args[0]);
                if(item != null){
                    showItemInfo(p, item);
                }else{
                    mensaje(p, "shop.notExist");
                }
            }
        }else
        if(args.length == 2){
            if(isCommand("comm.shop.search", args[0])){ // Buscar
                List<String> items = f.lookItems(args[1]);
                if(items.size() == 0){
                    mensaje(p, "shop.notExist");
                }else if(items.size() == 1){
                    showItemInfo(p, f.searchItem(items.get(0)));
                }else{
                    for(int i = 0; i < items.size(); i++){
                        items.set(i, String.format(Strings.getString("shop.searchItem"), items.get(i)));
                    }
                    mensaje(p, "shop.searchHead", args[1]);
                    mensaje(p, items);
                }
            }else{
                String item = f.searchItem(args[0]);
                if(item != null){
                    if(!isInteger(args[1])){
                        mensaje(p, "alert.noInteger");
                        return true;
                    }
                    f.shop(p, item, Integer.parseInt(args[1]));
                    mensaje(p, "shop.shoped");
                }else{
                    mensaje(p, "shop.notExist");
                }
            }
        }
        Factory.save();
        return true;
    }
    public void showItemInfo(Player p, String item){
    	String id = f.getId(item)+"";
    	if(f.haveData(item)){
    		id = id.concat(":"+f.getData(item));
    	}
        mensaje(p, "shop.itemInfo", item, new Econ().getPrecio(f.getPrecio(item)),
                f.getVel(item), id);
    }
}
