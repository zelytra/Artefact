package fr.zelytra.novaStructura.manager.structure;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.extent.clipboard.BlockArrayClipboard;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.*;
import com.sk89q.worldedit.function.operation.ForwardExtentCopy;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.world.World;
import fr.zelytra.novaStructura.NovaStructura;
import fr.zelytra.novaStructura.manager.logs.LogType;
import fr.zelytra.novaStructura.manager.structure.exception.ConfigParserException;
import fr.zelytra.novaStructura.manager.worldEdit.WorldEditHandler;
import fr.zelytra.novaStructura.utils.timer.Timer;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Structure {

    public static List<Structure> structureList = new ArrayList<>();

    private String name;
    private List<String> worlds = new ArrayList<>();
    private List<Biome> biomes = new ArrayList<>();
    private List<Material> whitelistedBlocks = new ArrayList<>();

    private File schematic, config;
    private Clipboard clipboard;

    private int offsetX = 0, offsetY = 0, offsetZ = 0;
    private double value = 1, outOf = 500;
    private int minHeight = 0, maxHeight = 255;
    private boolean placeAir = true, randomRotation = true, spawnOnWater = false, spawnOnLava = false;
    private boolean smartPaste = false;

    public Structure(String name) {
        this.name = name;
        structureList.add(this);
    }

    public Structure(File schematic) {

        try {
            ClipboardFormat format = ClipboardFormats.findByFile(schematic);
            ClipboardReader reader = format.getReader(new FileInputStream(schematic));

            this.clipboard = reader.read();
        } catch (IOException e) {
            NovaStructura.log("Failed to read schamtic: " + name, LogType.ERROR);
            return;
        }

        this.name = schematic.getName().replace(StructureFolder.SCHEMATIC.extension, "");
        structureList.add(this);

    }

    public Structure(File schematic, File conf) {

        try {
            ClipboardFormat format = ClipboardFormats.findByFile(schematic);
            ClipboardReader reader = format.getReader(new FileInputStream(schematic));

            this.clipboard = reader.read();
        } catch (IOException e) {
            NovaStructura.log("Failed to read schematic: " + name, LogType.ERROR);
            return;
        }

        this.name = schematic.getName().replace(StructureFolder.SCHEMATIC.extension, "");
        this.config = conf;

        if (!load(config)) {
            NovaStructura.log("Failed to load " + name + " structure config, please check syntax", LogType.ERROR);
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

        //WE Pasting
        WorldEditHandler pasteWE = new WorldEditHandler(location, clipboard);
        Timer timer = new Timer();
        pasteWE.pasteStructure();

        if (NovaStructura.debugMod)
            NovaStructura.log("Structure: " + name +
                    " | x: " + location.getBlockX() +
                    " y: " + location.getBlockY() +
                    " z: " + location.getBlockZ() +
                    " §8[" + timer.stop() + "]", LogType.INFO);

    }

    public boolean naturalDraw(Location spawnLocation) {

        if (Math.random() * outOf <= value) {

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

    public boolean generateFiles(@NotNull WorldEditHandler weh) {

        Region region;
        World world = BukkitAdapter.adapt(weh.getWorld());

        //Getting player selection
        region = weh.getSelection();

        if (region == null)
            return false;


        //Saving selection into clipboard
        BlockArrayClipboard clipboard = new BlockArrayClipboard(region);
        try (EditSession editSession = WorldEdit.getInstance().getEditSessionFactory().getEditSession(world, -1)) {
            ForwardExtentCopy forwardExtentCopy = new ForwardExtentCopy(editSession, region, clipboard, region.getMinimumPoint());
            Operations.complete(forwardExtentCopy);
        } catch (WorldEditException e) {
            return false;

        }
        this.clipboard = clipboard;

        schematic = new File(StructureManager.PATH + StructureFolder.SCHEMATIC + this.name + ".struct");

        if (!schematic.exists()) {
            try {
                schematic.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        try (ClipboardWriter writer = BuiltInClipboardFormat.SPONGE_SCHEMATIC.getWriter(new FileOutputStream(schematic))) {
            writer.write(clipboard);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return true;
    }

    public String getName() {
        return name;
    }

    @Nullable
    public static Structure getStructure(String name) {
        for (Structure structure : structureList)
            if (structure.name.equalsIgnoreCase(name)) return structure;
        return null;
    }

    public void save(@NotNull File conf) {
        try {

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

            configFile.set("properties.placeAir", placeAir);
            configFile.set("properties.randomRotation", randomRotation);
            configFile.set("properties.spawnOnWater", spawnOnWater);
            configFile.set("properties.spawnOnLava", spawnOnLava);
            configFile.set("properties.smartPaste", smartPaste);
            configFile.set("properties.whitelistBlock", whitelistedBlocks);

            configFile.save(conf);

        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }


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
            //biomes = configFile.getList("location.biomes");
            offsetX = configFile.getInt("location.offset.x");
            offsetY = configFile.getInt("location.offset.y");
            offsetZ = configFile.getInt("location.offset.z");

            placeAir = configFile.getBoolean("properties.placeAir");
            randomRotation = configFile.getBoolean("properties.randomRotation");
            spawnOnWater = configFile.getBoolean("properties.spawnOnWater");
            spawnOnLava = configFile.getBoolean("properties.spawnOnLava");
            smartPaste = configFile.getBoolean("properties.smartPaste");
            //whitelistedBlocks = configFile.getList("properties.whitelistBlock");
            return true;

        } catch (InvalidConfigurationException | IOException | ConfigParserException e) {
            NovaStructura.log(e.getLocalizedMessage(), LogType.ERROR);
            return false;
        }
    }

    @Override
    public String toString() {
        return "§6Name: §8" + name + "\n" +
                "§6Luck: §8" + value + "/" + outOf + "\n" +
                "§6OffSet: x=§8" + offsetX + " §6y=§8" + offsetY + " §6z=§8" + offsetZ + "\n" +
                "§6Worlds: §8" + worlds + "\n" +
                "§6Biomes: §8" + biomes + "\n" +
                "§6PlaceAir: " + (placeAir ? "§a" : "§c") + randomRotation + "\n" +
                "§6RandomRotation: " + (randomRotation ? "§a" : "§c") + randomRotation + "\n" +
                "§6SpawnOnLava: " + (spawnOnLava ? "§a" : "§c") + spawnOnLava + "\n" +
                "§6SpawnOnWater: " + (spawnOnWater ? "§a" : "§c") + spawnOnWater + "\n" +
                "§6SmartPaste: " + (smartPaste ? "§a" : "§c") + smartPaste + "\n";
    }
}
