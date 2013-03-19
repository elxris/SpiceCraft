package elxris.Useless.Listener;

import org.bukkit.Effect;
import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import elxris.Useless.Objects.Jobs;
import elxris.Useless.Utils.Chat;

public class JobsListener implements Listener{
    private static Jobs jobs;
    @EventHandler(priority = EventPriority.HIGH)
    public void onBreak(BlockBreakEvent event){
        if(event.isCancelled()){
            return;
        }
        if(event.getPlayer().getGameMode() != GameMode.SURVIVAL){
            return;
        }
        // TODO Revisar si pertenece algun job.
        // TODO jobs.check(String jugador, int BlockID);
        Chat.mensaje(event.getPlayer().getName(), event.getBlock().getTypeId()+"");
        Chat.mensaje(event.getPlayer().getName(), event.getBlock().getType().name());
        for(ItemStack item: event.getBlock().getDrops(event.getPlayer().getItemInHand())){
            Chat.mensaje(event.getPlayer().getName(), "."+item.getTypeId()+"");
            Chat.mensaje(event.getPlayer().getName(), item.getType().name());
            if(item.getTypeId() == 318){
                event.getBlock().setTypeId(0);
                event.getPlayer().getWorld().playEffect(event.getBlock().getLocation(), Effect.MOBSPAWNER_FLAMES, null);
            }
        }
        event.getBlock().setTypeId(0);
        
    }
    public static Jobs getJobs(){
        if(jobs == null){
            jobs = new Jobs();
        }
        return jobs;
    }
}
