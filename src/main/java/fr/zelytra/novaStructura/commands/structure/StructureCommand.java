package fr.zelytra.novaStructura.commands.structure;

import fr.zelytra.novaStructura.NovaStructura;
import fr.zelytra.novaStructura.manager.logs.LogType;
import fr.zelytra.novaStructura.manager.schematic.Schematic;
import fr.zelytra.novaStructura.manager.schematic.selector.Selector;
import fr.zelytra.novaStructura.manager.structure.Structure;
import fr.zelytra.novaStructura.utils.Message;
import fr.zelytra.novaStructura.utils.timer.Timer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class StructureCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (!(sender instanceof Player)){
            if (args.length > 0) {

                if (args[0].equalsIgnoreCase("delete")) {
                    Structure structure = Structure.getStructure(args[1]);

                    if (structure == null) {
                        NovaStructura.log("Any structure with this name found", LogType.ERROR);
                        return true;
                    }

                    structure.delete();
                    NovaStructura.log("Structure delete", LogType.ERROR);
                    return true;

                } else if (args[0].equalsIgnoreCase("reload")) {
                    NovaStructura.structureManager .reload();
                    NovaStructura.log("Structure loaded !", LogType.INFO);
                    return true;

                } else {

                    NovaStructura.log("Wrong argument", LogType.ERROR);
                    return true;

                }

            } else {
                NovaStructura.log("Wrong syntax command", LogType.ERROR);
                return true;
            }
        }

        Player player = (Player) sender;

        if (!player.isOp()){
            player.sendMessage(Message.PLAYER_PREFIX + "§cYou don't have permission to perform this command");
            return true;
        }

        if (args.length > 0) {

            if (args[0].equalsIgnoreCase("create") && args.length == 2) {

                if (Structure.exist(args[1])) {
                    player.sendMessage(Message.PLAYER_PREFIX + "§cA structure already have this name. Please choose another");
                    return true;
                }
                Bukkit.getScheduler().runTaskAsynchronously(NovaStructura.getInstance(), () -> {

                    Selector selector = Selector.getPlayerSelection(player);

                    if (selector != null && selector.isValidSelection()) {
                        Timer timer = new Timer();
                        Schematic schematic = new Schematic(selector, args[1]);
                        Structure structure = new Structure(schematic, args[1]);

                        player.sendMessage(Message.PLAYER_PREFIX + "§6Structure file saved :§9 " + structure.getName() + ".struct §8[" + timer.stop() + "]");

                    } else {
                        player.sendMessage(Message.PLAYER_PREFIX + "§cPlease make a selection.");
                    }

                });

                return true;


            } else if (args[0].equalsIgnoreCase("spawn")) {

                Structure structure = Structure.getStructure(args[1]);
                if (structure != null)
                    structure.paste(player.getLocation());

                return true;


            } else if (args[0].equalsIgnoreCase("delete")) {
                Structure structure = Structure.getStructure(args[1]);

                if (structure == null) {
                    player.sendMessage(Message.PLAYER_PREFIX + "§cAny structure with this name found");
                    return true;
                }

                structure.delete();
                player.sendMessage(Message.PLAYER_PREFIX + "§6Structure delete");
                return true;

            } else if (args[0].equalsIgnoreCase("reload")) {
                player.sendMessage(Message.PLAYER_PREFIX + "§6Reloading structures...");
                NovaStructura.structureManager.reload();
                player.sendMessage(Message.PLAYER_PREFIX + "§6Structure loaded !");
                return true;

            } else if (args[0].equalsIgnoreCase("info") && args.length == 2) {

                Structure structure = Structure.getStructure(args[1]);

                if (structure == null) {
                    player.sendMessage(Message.PLAYER_PREFIX + "§cAny structure with this name found");
                    return true;
                }

                player.sendMessage(structure.toString());
                return true;

            } else if (args[0].equalsIgnoreCase("wand")) {

                player.getInventory().addItem(new ItemStack(Material.FEATHER));
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
