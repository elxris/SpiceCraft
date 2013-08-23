package elxris.SpiceCraft.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

import elxris.SpiceCraft.SpiceCraft;
import elxris.SpiceCraft.Utils.Chat;

public class ReloadCommand implements CommandExecutor{

    @Override
    public boolean onCommand(CommandSender arg0, Command arg1, String arg2,
            String[] arg3) {
        Plugin plugin = SpiceCraft.plugin();
        PluginManager manager = plugin.getServer().getPluginManager();
        manager.disablePlugin(plugin);
        manager.enablePlugin(plugin);
        Chat.mensaje((Player) arg0, "cmd.reload");
        return true;
    }

}
