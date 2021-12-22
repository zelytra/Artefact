package fr.zelytra.novaStructura.manager.loottable;

import org.bukkit.inventory.ItemStack;

import java.io.Serializable;

public class Loot implements Serializable {

    private final ItemStack item;
    private final double luck;

    public Loot(ItemStack item, double luck) {
        this.item = item;
        this.luck = luck;
    }
}
