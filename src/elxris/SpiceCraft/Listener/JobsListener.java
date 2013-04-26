package elxris.SpiceCraft.Listener;

import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import elxris.SpiceCraft.Objects.Jobs;
import elxris.SpiceCraft.Utils.Chat;

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
        // TODO jobs.check(Player jugador, block BlockID);
        Chat.mensaje(event.getPlayer().getName(), event.getBlock().getTypeId()+"");
        Chat.mensaje(event.getPlayer().getName(), event.getBlock().getType().name());
        for(ItemStack item: event.getBlock().getDrops()){
            Chat.mensaje(event.getPlayer().getName(), "."+item.getTypeId()+"");
            Chat.mensaje(event.getPlayer().getName(), item.getType().name());
        }
        getJobs().check(event.getPlayer(), event.getBlock());
        
    }
    public static Jobs getJobs(){
        if(jobs == null){
            jobs = new Jobs();
        }
        return jobs;
    }
}
