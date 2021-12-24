package fr.zelytra.novaStructura.manager.loottable.parser;

import fr.zelytra.novaStructura.manager.loottable.Loot;
import net.kyori.adventure.text.Component;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class ItemParser {

    private final StringItem stringItem;
    private List<StringEnchant> stringEnchants;
    private List<StringPotion> stringPotions;

    private Loot loot;

    private final int luck;

    public ItemParser(StringItem item, int luck) {
        this.stringItem = item;
        this.luck = luck;
    }

    public ItemParser(StringItem item, int luck, StringEnchant[] enchants) {
        this.stringItem = item;
        this.stringEnchants = List.of(enchants);
        this.luck = luck;
    }

    public ItemParser(StringItem item, int luck, StringPotion[] potions) {
        this.stringItem = item;
        this.stringPotions = List.of(potions);
        this.luck = luck;
    }

    public boolean parse() {
        try {
            ItemStack item = new ItemStack(Material.getMaterial(stringItem.material().toUpperCase()), stringItem.amount());

            //Parsing enchant
            if (stringEnchants != null)
                for (StringEnchant stringEnchant : stringEnchants)
                    item.addUnsafeEnchantment(Enchantment.getByName(stringEnchant.enchantName().toUpperCase()), stringEnchant.level());

            //Parsing potion
            if (stringPotions != null) {
                PotionMeta potionMeta = (PotionMeta) item.getItemMeta();
                for (StringPotion potion : stringPotions) {

                    PotionEffectType potionEffectType = PotionEffectType.getByName(potion.effect().toUpperCase());
                    int duration = potion.duration();
                    int amplifier = potion.amplifier();

                    potionMeta.setColor(Color.fromRGB(ThreadLocalRandom.current().nextInt(0, 255 + 1), ThreadLocalRandom.current().nextInt(0, 255 + 1), ThreadLocalRandom.current().nextInt(0, 255 + 1)));
                    potionMeta.addCustomEffect(new PotionEffect(potionEffectType, duration, amplifier), true);
                    item.setItemMeta(potionMeta);

                    ItemMeta meta = item.getItemMeta();
                    meta.displayName(Component.text().content("Potion of " + potionEffectType.getName().toLowerCase()).build());
                    item.setItemMeta(meta);
                }
            }
            
            this.loot = new Loot(item,luck);
            return true;

        } catch (Exception ignored) {
            return false;
        }
    }

    public Loot getLoot() {
        return loot;
    }
}
