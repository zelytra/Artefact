package fr.zelytra.novaStructura.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.CommandBlock;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class Utils {

    public static List<String> dynamicTab(List<String> list, String arg) {
        List<String> finalList = new ArrayList<String>(list);
        for (String s : list) {
            if (!s.toLowerCase().startsWith(arg.toLowerCase())) {
                finalList.remove(s);
            }
        }
        Collections.sort(finalList);
        return finalList;
    }

    public static int getEmptySlots(Player player) {
        int i = 0;
        for (ItemStack is : player.getInventory().getContents()) {
            if (!(is == null))
                continue;
            i++;
        }
        for (ItemStack item : player.getInventory().getArmorContents()) {
            if (!(item == null))
                continue;
            i--;
        }
        return i;
    }

    public static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            @SuppressWarnings("unused")
            double d = Double.parseDouble(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }



    public static boolean isFullInv(Player player) {
        if (player.getInventory().firstEmpty() == -1) {
            return true;
        }
        return false;
    }

    public static void executeCommandBlock(Location loc, String command) {
        if (command == null) return;
        if (command.contains("/")) command = command.replaceFirst("/","");
        if (command.contains("~")) {
            int x = loc.getBlock().getX(), y = loc.getBlock().getY(), z = loc.getBlock().getZ();
            command = command.replaceFirst("~",x+"");
            command = command.replaceFirst("~",y+"");
            command = command.replaceFirst("~",z+"");
        }
        Bukkit.broadcastMessage(command);
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(),command);
        loc.getBlock().setType(Material.AIR);
    }

}
