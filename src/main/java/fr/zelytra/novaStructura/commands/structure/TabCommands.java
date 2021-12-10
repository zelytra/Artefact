package fr.zelytra.novaStructura.commands.structure;

import fr.zelytra.novaStructura.manager.structure.Structure;
import fr.zelytra.novaStructura.utils.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class TabCommands implements TabCompleter {

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        List<String> commandsList = new ArrayList<>();

        if (args.length == 1) {
            commandsList.add("create");
            commandsList.add("info");
            commandsList.add("delete");
            commandsList.add("spawn");
            commandsList.add("reload");
            commandsList = Utils.dynamicTab(commandsList, args[0]);

            return commandsList;
        }

        if (args.length == 2 && (args[0].equalsIgnoreCase("info") || args[0].equalsIgnoreCase("delete") || args[0].equalsIgnoreCase("spawn"))) {

            for (Structure structure : Structure.structureList)
                commandsList.add(structure.getName());

            commandsList = Utils.dynamicTab(commandsList, args[1]);
        }

        return commandsList;
    }

}
