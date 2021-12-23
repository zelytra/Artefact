package fr.zelytra.novaStructura.manager.loottable.parser;

public enum LootConf {
    SEPARATOR("."),

    DRAW("draw"),

    MATERIAL("material"),
    AMOUNT("amount"),
    LUCK("luck"),

    ENCHANT("enchant"),
    ENCHANT_TYPE("type"),
    ENCHANT_LEVEL("level"),

    POTION("potion"),
    POTION_TYPE("type"),
    POTION_DURATION("duration"),
    POTION_AMPLIFIER("amplifier");

    public String tag;

    LootConf(String tag) {
        this.tag = tag;
    }

    @Override
    public String toString() {
        return this.tag;
    }
}
