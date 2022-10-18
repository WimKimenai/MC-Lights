package wkimenai.mc-lights.setup;

import org.bukkit.command.PluginCommand;
import java.util.List;
import java.util.ArrayList;
import wkimenai.mc-lights.cmd.cmds.StaffCMD;
import wkimenai.mc-lights.cmd.cmds.GeneralCMD;
import wkimenai.mc-lights.cmd.cmds.ProjectorCMD;
import org.bukkit.command.CommandExecutor;
import wkimenai.mc-lights.cmd.cmds.EffectCmd;
import wkimenai.mc-lights.cmd.MultiExecutor;
import org.bukkit.Bukkit;

public class SetupCommand
{
    public SetupCommand() {
        final PluginCommand pluginCommand = Bukkit.getPluginCommand("mc-lights");
        final MultiExecutor executor = new MultiExecutor();
        executor.addExecutor((CommandExecutor)new EffectCmd());
        executor.addExecutor((CommandExecutor)new ProjectorCMD());
        executor.addExecutor((CommandExecutor)new GeneralCMD());
        executor.addExecutor((CommandExecutor)new StaffCMD().registerRemoveMode());
        final ArrayList<String> aliases = new ArrayList<String>();
        aliases.add("mc-lights");
        aliases.add("pnc");
        pluginCommand.setAliases((List)aliases);
        pluginCommand.setExecutor((CommandExecutor)executor);
    }
}
