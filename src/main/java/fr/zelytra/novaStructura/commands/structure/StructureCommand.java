package fr.zelytra.novaStructura.commands.structure;

import fr.zelytra.novaStructura.manager.structure.Structure;
import fr.zelytra.novaStructura.manager.worldEdit.WorldEditHandler;
import fr.zelytra.novaStructura.utils.Message;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class StructureCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (!(sender instanceof Player)) return false;

        Player player = (Player) sender;

        if (args.length > 0) {

            if (args[0].equalsIgnoreCase("create") && args.length == 2) {

                WorldEditHandler weh = new WorldEditHandler(player);
                Structure structure = new Structure(args[1]);

                if (structure.generateFiles(weh)) {
                    player.sendMessage(Message.PLAYER_PREFIX + "§6Structure file saved :§9 " + structure.getName() + ".struct");
                } else {
                    player.sendMessage(Message.PLAYER_PREFIX + "§cPlease make a selection.");
                }
                return true;


            } else if (args[0].equalsIgnoreCase("spawn")) {

                Structure structure = Structure.getStructure(args[1]);
                if (structure!=null)
                    structure.paste(player.getLocation());

                return true;


            } else if (args[0].equalsIgnoreCase("delete")) {

                return true;

            } else {

                player.sendMessage(Message.PLAYER_PREFIX + "§cWrong argument");
                return true;

            }

        } else {
            player.sendMessage(Message.PLAYER_PREFIX + "§cWrong syntax command");
            return true;
        }


    }
}
