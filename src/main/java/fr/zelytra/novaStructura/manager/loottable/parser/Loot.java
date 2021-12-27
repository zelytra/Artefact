package fr.zelytra.novaStructura.manager.loottable.parser;

import fr.zelytra.novaStructura.manager.loottable.content.StringEnchant;
import fr.zelytra.novaStructura.manager.loottable.content.StringItem;
import fr.zelytra.novaStructura.manager.loottable.content.StringPotion;
import net.kyori.adventure.text.Component;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Loot {

    private final StringItem stringItem;
    private List<StringEnchant> stringEnchants;
    private List<StringPotion> stringPotions;

    private final int luck;

    public Loot(StringItem item, int luck) {
        this.stringItem = item;
        this.luck = luck;
    }

    public Loot(StringItem item, int luck, StringEnchant[] enchants) {
        this.stringItem = item;
        this.stringEnchants = List.of(enchants);
        this.luck = luck;
    }

    public Loot(StringItem item, int luck, StringPotion[] potions) {
        this.stringItem = item;
        this.stringPotions = List.of(potions);
        this.luck = luck;
    }

    public ItemStack parse() {
        try {
            ItemStack item = new ItemStack(Material.getMaterial(stringItem.getMaterial().toUpperCase()), stringItem.getAmount());

            //Parsing enchant
            if (stringEnchants != null)
                for (StringEnchant stringEnchant : stringEnchants) {
                    if (item.getType() != Material.ENCHANTED_BOOK)
                        item.addUnsafeEnchantment(Enchantment.getByName(stringEnchant.getEnchantName().toUpperCase()), stringEnchant.getLevel());
                    else
                        item = bookEnchantedItemStack(item, Enchantment.getByName(stringEnchant.getEnchantName().toUpperCase()), stringEnchant.getLevel());
                }

            //Parsing potion
            if (stringPotions != null) {
                PotionMeta potionMeta = (PotionMeta) item.getItemMeta();
                for (StringPotion potion : stringPotions) {

                    PotionEffectType potionEffectType = PotionEffectType.getByName(potion.getEffect().toUpperCase());
                    int duration = potion.getDuration();
                    int amplifier = potion.getAmplifier();

                    potionMeta.setColor(Color.fromRGB(ThreadLocalRandom.current().nextInt(0, 255 + 1), ThreadLocalRandom.current().nextInt(0, 255 + 1), ThreadLocalRandom.current().nextInt(0, 255 + 1)));
                    potionMeta.addCustomEffect(new PotionEffect(potionEffectType, duration, amplifier), true);
                    item.setItemMeta(potionMeta);

                    ItemMeta meta = item.getItemMeta();
                    meta.displayName(Component.text().content("Potion of " + potionEffectType.getName().toLowerCase()).build());
                    item.setItemMeta(meta);
                }
            }
            return item;

        } catch (Exception ignored) {
            ignored.printStackTrace();
            return null;
        }
    }

    public int getLuck() {
        return luck;
    }

    private ItemStack bookEnchantedItemStack(ItemStack item, Enchantment enchantment, int lvl) {
        EnchantmentStorageMeta meta = (EnchantmentStorageMeta) item.getItemMeta();
        meta.addStoredEnchant(enchantment, lvl, false);
        item.setItemMeta(meta);
        return item;
    }
}
