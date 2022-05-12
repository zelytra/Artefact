package fr.zelytra.novaStructura.commands;

import org.bukkit.Bukkit;
import org.bukkit.block.CommandBlock;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class Test implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (!(sender instanceof Player)) return false;

        Player player = (Player) sender;
        if (player.getLocation().getBlock().getState() instanceof CommandBlock) {
            CommandBlock cmd = (CommandBlock) player.getLocation().getBlock().getState();
            Bukkit.broadcastMessage(cmd.getCommand() + "");
        }

        return true;
    }
}
