package gg.uhc.gamestatus;

import com.google.common.base.Joiner;
import com.google.common.base.Optional;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class StatusCommand implements CommandExecutor {

    public static final String PERMISSION = "uhc.gamestatus";

    protected final MotdController controller;

    public StatusCommand(MotdController controller) {
        this.controller = controller;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (controller == null) {
            commandSender.sendMessage(ChatColor.RED + "There was an error loading the configuration at startup, check console for more information");
            return true;
        }

        if (!commandSender.hasPermission(PERMISSION)) {
            commandSender.sendMessage(ChatColor.RED + "You do not have permission to use this command");
            return true;
        }

        if (args.length == 0) {
            controller.clearMotd();
            commandSender.sendMessage(ChatColor.GOLD + "Cleared the game status MOTD, using the server's MOTD");
            return true;
        }

        String key = Joiner.on(" ").join(args);

        Optional<String> motd = controller.chooseMotd(key);

        if (motd.isPresent()) {
            commandSender.sendMessage(ChatColor.GOLD + "Game status MOTD status changed to: " + ChatColor.RESET + motd.get());
        } else {
            commandSender.sendMessage(ChatColor.RED + "I don't know how to display `" + key + "`");
        }

        return true;
    }
}
