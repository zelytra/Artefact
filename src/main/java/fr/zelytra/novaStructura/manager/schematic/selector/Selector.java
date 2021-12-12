package fr.zelytra.novaStructura.manager.schematic.selector;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class Selector {

    public static List<Selector> selectorList = new ArrayList<>();
    public static Material selectorMaterial = Material.FEATHER;

    public Location corner1, corner2;
    private final String player;

    public Selector(Player player) {
        this.player = player.getName();
        selectorList.add(this);
    }

    public void setCorner1(Location corner1) {
        this.corner1 = corner1;
    }

    public void setCorner2(Location corner2) {
        this.corner2 = corner2;
    }

    public boolean isValidSelection() {
        if (corner1 == null || corner2 == null) return false;
        return corner2.getWorld() == corner1.getWorld();
    }

    public static @Nullable Selector getPlayerSelection(Player player) {

        for (Selector selector : selectorList)
            if (player.getName().equalsIgnoreCase(selector.player))
                return selector;


        return null;
    }


}
