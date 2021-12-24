package fr.zelytra.novaStructura.commands.structure;

import fr.zelytra.novaStructura.NovaStructura;
import fr.zelytra.novaStructura.manager.logs.LogType;
import fr.zelytra.novaStructura.manager.structure.Structure;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class StructureConsoleCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) return false;

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
                NovaStructura.structureManager.reload();
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
}
