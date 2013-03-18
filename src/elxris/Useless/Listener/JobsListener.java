package elxris.Useless.Listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import elxris.Useless.Objects.Jobs;

public class JobsListener implements Listener{
    private static Jobs jobs;
    @EventHandler(priority = EventPriority.HIGH)
    public void onBreak(BlockBreakEvent event){
        if(!event.isCancelled()){
            // Revisar si pertenece algun job.
            if(event.getBlock().getTypeId() == 1){
                event.getBlock().setTypeId(0);
            }
        }
    }
    public static Jobs getJobs(){
        if(jobs == null){
            jobs = new Jobs();
        }
        return jobs;
    }
}
