package fr.zelytra.novaStructura.manager.structure;

import fr.zelytra.novaStructura.NovaStructura;
import fr.zelytra.novaStructura.manager.logs.LogType;
import fr.zelytra.novaStructura.manager.loottable.LootTable;
import fr.zelytra.novaStructura.manager.loottable.parser.Loot;
import fr.zelytra.novaStructura.manager.schematic.Schematic;
import fr.zelytra.novaStructura.manager.structure.exception.ConfigParserException;
import fr.zelytra.novaStructura.utils.timer.Timer;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Structure {

    public static List<Structure> structureList = new ArrayList<>();

    private String name;
    private List<String> worlds = new ArrayList<>();
    private List<Biome> biomes = new ArrayList<>();
    private List<Material> whitelistedBlocks = new ArrayList<>();


    private File config;
    private Schematic schematic;

    private LootTable lootTable;
    private List<Material> lootContainer = new ArrayList<>();

    {
        lootContainer.add(Material.CHEST);
    }

    private int offsetX = 0, offsetY = 0, offsetZ = 0;
    private double value = 1, outOf = 500;
    private int minHeight = 0, maxHeight = 255;
    private boolean placeAir = true, randomRotation = true, spawnOnWater = false, spawnOnLava = false, spawnInCave = false;
    private boolean smartPaste = false;

    public Structure(Schematic schematic, String name) {

        this.schematic = schematic;
        this.name = name;
        this.config = save();

        schematic.save();
        structureList.add(this);

    }

    public Structure(File schematic) {

        this.schematic = Schematic.loadFromFile(schematic);
        this.name = schematic.getName().replace(StructureFolder.SCHEMATIC.extension, "");
        structureList.add(this);
        this.config = save();

    }

    public Structure(File schematic, File conf) {

        this.schematic = Schematic.loadFromFile(schematic);
        this.name = schematic.getName().replace(StructureFolder.SCHEMATIC.extension, "");
        this.config = conf;

        if (!load(config)) {
            NovaStructura.log("[" + name + "] Failed to load structure config, please check syntax", LogType.ERROR);
            StructureManager.structureCount--;
            return;
        }

        structureList.add(this);

    }

    public void paste(@NotNull Location location) {

        //Process offset
        location.setZ(location.getZ() + offsetZ);
        location.setY(location.getY() + offsetY);
        location.setX(location.getX() + offsetX);


        Timer timer = new Timer();
        schematic.paste(location, this);

        if (NovaStructura.debugMod)
            NovaStructura.log("Structure: " + name +
                    " | x: " + location.getBlockX() +
                    " y: " + location.getBlockY() +
                    " z: " + location.getBlockZ() +
                    " §8[" + timer.stop() + "]", LogType.INFO);


    }

    public boolean naturalDraw(Location spawnLocation) {

        if (Math.random() * outOf <= value) {

            //TODO Handle custom biome with NMS FFS...
            if (!biomes.isEmpty() && !biomes.contains(spawnLocation.getWorld().getBiome(spawnLocation)))
                return false;


            if (!worlds.isEmpty() && !worlds.contains(spawnLocation.getWorld().getName()))
                return false;


            if (!whitelistedBlocks.isEmpty() && !whitelistedBlocks.contains(spawnLocation.getBlock().getType()))
                return false;

            return true;
        }

        //TODO check water or lava lack
        return false;
    }

    public String getName() {
        return name;
    }

    public LootTable getLootTable() {
        return lootTable;
    }

    public List<Material> getLootContainer() {
        return lootContainer;
    }

    public int getMinHeight() {
        return minHeight;
    }

    public static List<Structure> getStructureList() {
        return structureList;
    }

    public boolean isPlaceAir() {
        return placeAir;
    }

    public boolean isRandomRotation() {
        return randomRotation;
    }

    public int getMaxHeight() {
        return maxHeight;
    }

    public boolean isSpawnOnWater() {
        return spawnOnWater;
    }

    public boolean isSpawnOnLava() {
        return spawnOnLava;
    }

    public boolean isSpawnInCave() {
        return spawnInCave;
    }

    @Nullable
    public static Structure getStructure(String name) {
        for (Structure structure : structureList)
            if (structure.name.equalsIgnoreCase(name)) return structure;
        return null;
    }

    public File save() {

        try {
            File conf = new File(StructureManager.PATH + StructureFolder.CONFIG + this.name + StructureFolder.CONFIG.extension);
            conf.createNewFile();

            FileConfiguration configFile = new YamlConfiguration();
            configFile.load(conf);

            configFile.set("luck.value", value);
            configFile.set("luck.outOf", outOf);

            configFile.set("location.worlds", worlds);
            configFile.set("location.biomes", biomes);
            configFile.set("location.offset.x", offsetX);
            configFile.set("location.offset.y", offsetY);
            configFile.set("location.offset.z", offsetZ);
            configFile.set("location.minHeight", minHeight);
            configFile.set("location.maxHeight", maxHeight);

            configFile.set("properties.placeAir", placeAir);
            configFile.set("properties.randomRotation", randomRotation);
            configFile.set("properties.spawnOnWater", spawnOnWater);
            configFile.set("properties.spawnOnLava", spawnOnLava);
            configFile.set("properties.spawnInCave", spawnInCave);
            configFile.set("properties.smartPaste", smartPaste);
            configFile.set("properties.whitelistBlock", whitelistedBlocks);

            configFile.set("lootTable", lootTable != null ? lootTable.getName() : "");
            configFile.set("lootContainer", ConfParser.unparseMaterial(lootContainer));

            configFile.save(conf);
            return conf;
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
        return null;

    }

    private boolean load(File config) {
        try {

            FileConfiguration configFile = new YamlConfiguration();
            configFile.load(config);

            value = configFile.getDouble("luck.value");

            if (value <= 0)
                throw new ConfigParserException("Invalid luck value detected. Must be higher than 0");

            outOf = configFile.getDouble("luck.outOf");
            if (outOf <= 0 || outOf < value)
                throw new ConfigParserException("Invalid outOf value detected. Must be higher than 0 and higher than luck value");

            worlds = (List<String>) configFile.getList("location.worlds");
            biomes = ConfParser.parseBiome((List<String>) configFile.getList("location.biomes"));
            whitelistedBlocks = (List<Material>) configFile.getList("properties.whitelistBlock");

            offsetX = configFile.getInt("location.offset.x");
            offsetY = configFile.getInt("location.offset.y");
            offsetZ = configFile.getInt("location.offset.z");
            minHeight = configFile.getInt("location.minHeight");
            maxHeight = configFile.getInt("location.maxHeight");

            placeAir = configFile.getBoolean("properties.placeAir");
            randomRotation = configFile.getBoolean("properties.randomRotation");
            spawnOnWater = configFile.getBoolean("properties.spawnOnWater");
            spawnOnLava = configFile.getBoolean("properties.spawnOnLava");
            spawnInCave = configFile.getBoolean("properties.spawnInCave");
            smartPaste = configFile.getBoolean("properties.smartPaste");

            if (!configFile.getString("lootTable").isEmpty() && LootTable.exist(configFile.getString("lootTable")))
                lootTable = new LootTable(configFile.getString("lootTable"));
            else if (!configFile.getString("lootTable").isEmpty())
                throw new ConfigParserException("[" + name + "] Any loottable with this name, please check config");

            if (configFile.getList("lootContainer") != null)
                lootContainer = ConfParser.parseMaterial((List<String>) configFile.getList("lootContainer"));


            return true;

        } catch (InvalidConfigurationException | IOException | ConfigParserException e) {
            NovaStructura.log(e.getLocalizedMessage(), LogType.ERROR);
            return false;
        }
    }

    @Override
    public String toString() {
        return "§8---------------§6 [ Structure Data ] §8---------------" + "\n" +
                "§8⬤ §6Name: §8" + name + "\n" +
                "§8⬤ §6LootTable: §8" + (lootTable != null ? lootTable.getName() : "any") + "\n" +
                "§8⬤ §6LootContainer: §8" + lootContainer + "\n" +
                "§8⬤ §6Luck: §8" + value + "/" + outOf + "\n" +
                "§8⬤ §6OffSet: x=§8" + offsetX + " §6y=§8" + offsetY + " §6z=§8" + offsetZ + "\n" +
                "§8⬤ §6Height: Ymax=§8" + maxHeight + " §6Ymin=§8" + minHeight + "\n" +
                "§8⬤ §6Worlds: §8" + worlds + "\n" +
                "§8⬤ §6Biomes: §8" + biomes + "\n" +
                "§8⬤ §6PlaceAir: " + (placeAir ? "§a" : "§c") + randomRotation + "\n" +
                "§8⬤ §6RandomRotation: " + (randomRotation ? "§a" : "§c") + randomRotation + "\n" +
                "§8⬤ §6SpawnOnLava: " + (spawnOnLava ? "§a" : "§c") + spawnOnLava + "\n" +
                "§8⬤ §6SpawnOnWater: " + (spawnOnWater ? "§a" : "§c") + spawnOnWater + "\n" +
                "§8⬤ §6SpawnInCave: " + (spawnInCave ? "§a" : "§c") + spawnInCave + "\n" +
                "§8⬤ §6SmartPaste: " + (smartPaste ? "§a" : "§c") + smartPaste + "\n";
    }

    public static boolean exist(String name) {
        for (Structure structure : structureList)
            if (structure.name.equalsIgnoreCase(name))
                return true;
        return false;
    }

    public void delete() {
        config.delete();
        schematic.delete();
        structureList.remove(this);
    }

    public ItemStack[] drawLoot() {
        ItemStack[] content = new ItemStack[27];

        for (int i = 0; i <= lootTable.getDraw(); i++) {

            int slotRandom = (int) (Math.random() * (content.length));

            if (content[slotRandom] != null) continue;


            double random = Math.random() * 100;

            for (Loot loot : lootTable.getLoots()) {

                if (loot.getLuck() > random) {
                    content[slotRandom] = loot.parse();
                    break;
                }
                random -= loot.getLuck();

            }

        }
        return content;
    }
}
