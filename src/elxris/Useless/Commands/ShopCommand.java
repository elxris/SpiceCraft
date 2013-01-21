package elxris.Useless.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import elxris.Useless.Objects.Factory;
import elxris.Useless.Utils.Econ;

public class ShopCommand extends Comando{
    public Factory f;
    public ShopCommand() {
        f = new Factory();
    }
    @Override
    public boolean onCommand(CommandSender player, Command command, String label, String[] args) {
        if(!(player instanceof Player)){
            return false;
        }
        Player p = (Player)player;
        if(args.length == 0){
            mensaje(p, "shop.info");
        }else
        if(args.length == 1){
            String item = f.searchItem(p, args[0]);
            if(item != null){
                mensaje(p, "shop.itemInfo", item, new Econ().getPrecio(f.getPrecio(item)),
                        f.getVel(item), f.getId(item));
            }else{
                mensaje(p, "shop.noExist");
            }
        }else
        if(args.length == 2){
            if(isCommand("comm.shop.sell", args[0])){
                // TODO comando venta.
            }else{
                String item = f.searchItem(p, args[0]);
                if(item != null){
                    if(!isInteger(args[1])){
                        mensaje(p, "alert.noInteger");
                        return true;
                    }
                    f.shop(p, item, Integer.parseInt(args[1]));
                    mensaje(p, "shop.shoped");
                }else{
                    mensaje(p, "shop.noExist");
                }
            }
        }
        return true;
    }

}
