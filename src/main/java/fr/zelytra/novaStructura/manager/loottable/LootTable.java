package fr.zelytra.novaStructura.manager.loottable;

import fr.zelytra.novaStructura.NovaStructura;
import fr.zelytra.novaStructura.manager.logs.LogType;
import fr.zelytra.novaStructura.manager.loottable.content.DynamicRange;
import fr.zelytra.novaStructura.manager.loottable.content.StringEnchant;
import fr.zelytra.novaStructura.manager.loottable.content.StringItem;
import fr.zelytra.novaStructura.manager.loottable.content.StringPotion;
import fr.zelytra.novaStructura.manager.loottable.parser.Loot;
import fr.zelytra.novaStructura.manager.loottable.parser.LootConf;
import fr.zelytra.novaStructura.manager.structure.StructureFolder;
import fr.zelytra.novaStructura.manager.structure.StructureManager;
import fr.zelytra.novaStructura.manager.structure.exception.ConfigParserException;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class LootTable implements Serializable {

    private final String name;
    private int draw;
    private final List<Loot> loots;

    public LootTable(String name) {
        this.name = name;
        this.loots = loadFromFile(name);
    }

    public static boolean exist(String lootTable) {
        File pluginFolder = new File(StructureManager.PATH + File.separator + StructureFolder.LOOTS);
        for (File file : pluginFolder.listFiles())
            if (file.getName().contains(lootTable))
                return true;
        return false;

    }

    public void add(Loot loot) {
        loots.add(loot);
    }

    public String getName() {
        return name;
    }

    public List<Loot> getLoots() {
        return loots;
    }

    public int getDraw() {
        return draw;
    }

    public DynamicRange getRangedAmount(String range) {
        try {

            int level = Integer.parseInt(range);
            return new DynamicRange(level, level);

        } catch (NumberFormatException ignored) {

            String[] ranges = range.replace("[", "").replace("]", "").split(";");

            try {

                int min = Integer.parseInt(ranges[0]) , max = Integer.parseInt(ranges[1]) ;
                System.out.println(min + " " + max);
                return new DynamicRange(min, max);

            } catch (NumberFormatException ignored2) {
                return new DynamicRange(1, 1);
            }
        }

    }

    private List<Loot> loadFromFile(String name) {
        File pluginFolder = new File(StructureManager.PATH + File.separator + StructureFolder.LOOTS);
        List<Loot> loots = new ArrayList<>();

        if (pluginFolder.listFiles().length <= 0) return loots;

        try {
            for (File file : pluginFolder.listFiles()) {
                if (file.getName().contains(name)) {

                    FileConfiguration configFile = new YamlConfiguration();
                    configFile.load(file);

                    this.draw = configFile.getInt("draw");

                    if (!(draw >= 0 && draw <= 27))
                        throw new ConfigParserException("[" + name + "] Failed to parse " + name + ", please check draw number (must be between 0 and 27)");


                    for (String itemTag : configFile.getKeys(false)) {

                        if (itemTag.equalsIgnoreCase("draw")) continue;

                        StringItem item = parseItem(itemTag, configFile);

                        //Enchant parser
                        StringEnchant[] enchants = parseEnchant(itemTag, configFile);

                        StringPotion[] potions = new StringPotion[0];

                        Loot loot;

                        if (enchants.length > 0)
                            loot = new Loot(item,
                                    configFile.getInt(itemTag + LootConf.SEPARATOR + LootConf.LUCK),
                                    enchants != null ? enchants : new StringEnchant[0]);
                        else if (potions.length > 0)
                            loot = new Loot(item,
                                    configFile.getInt(itemTag + LootConf.SEPARATOR + LootConf.LUCK),
                                    potions != null ? potions : new StringPotion[0]);
                        else
                            loot = new Loot(item,
                                    configFile.getInt(itemTag + LootConf.SEPARATOR + LootConf.LUCK));

                        if (loot.parse() != null)
                            loots.add(loot);
                        else
                            throw new ConfigParserException("[" + name + "] Failed to parse " + item.getMaterial() + ", please check config file");

                    }

                }
            }

        } catch (IOException | InvalidConfigurationException | ConfigParserException e) {
            NovaStructura.log(e.getLocalizedMessage(), LogType.ERROR);
        }

        return loots;
    }

    private @NotNull StringItem parseItem(String path, @NotNull FileConfiguration conf) throws ConfigParserException {
        DynamicRange dynamicRange;
        StringItem item;
        if (conf.getString(path + LootConf.SEPARATOR + LootConf.AMOUNT).contains(";")) {
            dynamicRange = getRangedAmount(conf.getString(path + LootConf.SEPARATOR + LootConf.AMOUNT));

            if (dynamicRange.min() <= 0 || dynamicRange.max() <= 0)
                throw new ConfigParserException("[" + name + "] Failed to parse " + name + ", please check item amount syntax");

            item = new StringItem(conf.getString(path + LootConf.SEPARATOR + LootConf.MATERIAL), dynamicRange);
        } else
            item = new StringItem(conf.getString(path + LootConf.SEPARATOR + LootConf.MATERIAL), conf.getInt(path + LootConf.SEPARATOR + LootConf.AMOUNT));

        if (item.getAmount() == 0)
            throw new ConfigParserException("[" + name + "] Failed to parse " + name + ", please check item amount syntax");

        return item;
    }

    private StringEnchant[] parseEnchant(String path, @NotNull FileConfiguration conf) throws ConfigParserException {

        StringEnchant[] enchants = new StringEnchant[0];

        if (conf.getConfigurationSection(path + LootConf.SEPARATOR + LootConf.ENCHANT) != null) {
            if (conf.getConfigurationSection(path + LootConf.SEPARATOR + LootConf.ENCHANT).getKeys(true).size() == 2) {


                if (conf.getString(path + LootConf.SEPARATOR + LootConf.ENCHANT + LootConf.SEPARATOR + LootConf.ENCHANT_LEVEL).contains(";")) {

                    DynamicRange range = getRangedAmount(conf.getString(path + LootConf.SEPARATOR + LootConf.ENCHANT + LootConf.SEPARATOR + LootConf.ENCHANT_LEVEL));
                    enchants = new StringEnchant[]{new StringEnchant(conf.getString(path + LootConf.SEPARATOR + LootConf.ENCHANT + LootConf.SEPARATOR + LootConf.ENCHANT_TYPE), range)};

                    if (range.min() <= 0 || range.max() <= 0)
                        throw new ConfigParserException("[" + name + "] Failed to parse " + name + ", please check enchant level amount syntax");

                } else {
                    int level = conf.getInt(path + LootConf.SEPARATOR + LootConf.ENCHANT + LootConf.SEPARATOR + LootConf.ENCHANT_LEVEL);

                    if (level == 0)
                        throw new ConfigParserException("[" + name + "] Failed to parse " + name + ", please check enchant level amount syntax");

                    enchants = new StringEnchant[]{new StringEnchant(conf.getString(path + LootConf.SEPARATOR + LootConf.ENCHANT + LootConf.SEPARATOR + LootConf.ENCHANT_TYPE), level)};
                }


            } else {
                enchants = new StringEnchant[conf.getConfigurationSection(path + LootConf.SEPARATOR + LootConf.ENCHANT).getKeys(false).size()];
                int enchantCount = 0;

                for (String enchantTag : conf.getConfigurationSection(path + LootConf.SEPARATOR + LootConf.ENCHANT).getKeys(false)) {

                    String pathToEnchant = path + LootConf.SEPARATOR + LootConf.ENCHANT + LootConf.SEPARATOR + enchantTag + LootConf.SEPARATOR;

                    if (conf.getString(pathToEnchant + LootConf.ENCHANT_LEVEL).contains(";")) {

                        DynamicRange range = getRangedAmount(conf.getString(pathToEnchant + LootConf.ENCHANT_LEVEL));
                        enchants[enchantCount] = new StringEnchant(conf.getString(pathToEnchant + LootConf.ENCHANT_TYPE), range);

                        if (range.min() <= 0 || range.max() <= 0)
                            throw new ConfigParserException("[" + name + "] Failed to parse " + name + ", please check enchant level amount syntax");

                    } else {
                        int level = conf.getInt(pathToEnchant + LootConf.ENCHANT_LEVEL);

                        if (level == 0)
                            throw new ConfigParserException("[" + name + "] Failed to parse " + name + ", please check enchant level amount syntax");

                        enchants[enchantCount] = new StringEnchant(conf.getString(path + LootConf.SEPARATOR + LootConf.ENCHANT + LootConf.SEPARATOR + enchantTag + LootConf.SEPARATOR + LootConf.ENCHANT_TYPE), level);
                    }
                    enchantCount++;
                }
            }
        }
        for (StringEnchant enchant : enchants)
            System.out.println(enchant);
        return enchants;
    }
}
