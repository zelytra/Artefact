package fr.zelytra.novaStructura.manager.loottable.content;

public class StringEnchant {

    private final String enchantName;

    private int level;
    private DynamicRange lvlRange;

    public StringEnchant(String enchantName, int level) {
        this.enchantName = enchantName;
        this.level = level;
    }

    public StringEnchant(String enchantName, DynamicRange lvlRange) {
        this.enchantName = enchantName;
        this.lvlRange = lvlRange;
    }

    public String getEnchantName() {
        return enchantName;
    }

    public int getLevel() {
        if (lvlRange != null)
            return lvlRange.drawValue();
        else
            return level;
    }

    @Override
    public String toString() {
        return "EnchantName: " + enchantName + " Lvl: " + getLevel();
    }
}
