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

    private List<Loot> loadFromFile(String name) {
        File pluginFolder = new File(StructureManager.PATH + File.separator + StructureFolder.LOOTS);
        if (pluginFolder.listFiles().length <= 0) return new ArrayList<>();

        try {
            for (File file : pluginFolder.listFiles()) {
                if (file.getName().contains(name)) {

                    FileConfiguration configFile = new YamlConfiguration();
                    configFile.load(file);

                    for (String itemTag : configFile.getKeys(false)) {

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

                        if (parser.parse())
                            this.loots.add(parser.getLoot());
                        else
                            NovaStructura.log("Failed to parse " + item.material() + ", please check config file (item skip in loottable)", LogType.ERROR);

                    }

                }
            }

        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }

        return new ArrayList<>();
    }
}
