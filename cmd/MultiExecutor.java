package wkimenai.mc-lights.cmd;

import java.util.Collections;
import java.util.Iterator;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.command.CommandExecutor;

public class MultiExecutor implements CommandExecutor
{
    private List<CommandExecutor> executors;
    
    public MultiExecutor() {
        this.executors = new ArrayList<CommandExecutor>();
    }
    
    public boolean onCommand(final CommandSender commandSender, final Command command, final String s, final String[] array) {
        Boolean b = null;
        final Iterator<CommandExecutor> iterator = this.executors.iterator();
        while (iterator.hasNext()) {
            if (!iterator.next().onCommand(commandSender, command, s, array)) {
                b = false;
            }
            else {
                if (b != null) {
                    continue;
                }
                b = true;
            }
        }
        return b != null && b;
    }
    
    public void addExecutor(final CommandExecutor commandExecutor) {
        this.executors.add(commandExecutor);
    }
    
    public void removeExecutor(final CommandExecutor commandExecutor) {
        this.executors.remove(commandExecutor);
    }
    
    public List<CommandExecutor> getExecutors() {
        return Collections.unmodifiableList((List<? extends CommandExecutor>)this.executors);
    }
}
