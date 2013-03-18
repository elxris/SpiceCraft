package elxris.Useless.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import elxris.Useless.Listener.JobsListener;
import elxris.Useless.Objects.Jobs;

public class JobsCommand extends Comando{
    private static Jobs jobs;
    @Override
    public boolean onCommand(CommandSender arg0, Command arg1, String arg2,
            String[] arg3) {
        // TODO Auto-generated method stub
        return false;
    }
    public Jobs getJobs() {
        if(jobs == null){
            jobs = JobsListener.getJobs();
        }
        return jobs;
    }
}
