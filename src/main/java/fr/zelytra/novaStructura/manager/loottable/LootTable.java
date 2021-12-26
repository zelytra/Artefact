package fr.zelytra.novaStructura.manager.loottable;

import fr.zelytra.novaStructura.NovaStructura;
import fr.zelytra.novaStructura.manager.logs.LogType;
import fr.zelytra.novaStructura.manager.loottable.parser.*;
import fr.zelytra.novaStructura.manager.structure.StructureFolder;
import fr.zelytra.novaStructura.manager.structure.StructureManager;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

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
                    if (!(draw >= 0 && draw <= 27)) {
                        NovaStructura.log("[" + name + "] Failed to parse " + name + ", please check draw number (must be between 0 and 27)", LogType.ERROR);
                        break;
                    }

                    for (String itemTag : configFile.getKeys(false)) {

                        if (itemTag.equalsIgnoreCase("draw")) continue;

                        StringItem item = new StringItem(configFile.getString(itemTag + LootConf.SEPARATOR + LootConf.MATERIAL),
                                configFile.getInt(itemTag + LootConf.SEPARATOR + LootConf.AMOUNT));

                        StringEnchant[] enchants = new StringEnchant[0];
                        StringPotion[] potions = new StringPotion[0];

                        ItemParser parser;

                        if (enchants.length > 0)
                            parser = new ItemParser(item,
                                    configFile.getInt(itemTag + LootConf.SEPARATOR + LootConf.LUCK),
                                    enchants != null ? enchants : new StringEnchant[0]);
                        else if (potions.length > 0)
                            parser = new ItemParser(item,
                                    configFile.getInt(itemTag + LootConf.SEPARATOR + LootConf.LUCK),
                                    potions != null ? potions : new StringPotion[0]);
                        else
                            parser = new ItemParser(item,
                                    configFile.getInt(itemTag + LootConf.SEPARATOR + LootConf.LUCK));

                        if (parser.parse() && parser.getLoot() != null)
                            loots.add(parser.getLoot());
                        else
                            NovaStructura.log("[" + name + "] Failed to parse " + item.material() + ", please check config file (item skip in loottable)", LogType.ERROR);

                    }

                }
            }

        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }

        return loots;
    }
}
