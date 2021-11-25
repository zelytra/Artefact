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
import fr.zelytra.novaStructura.manager.worldEdit.WorldEditHandler;
import fr.zelytra.novaStructura.utils.timer.Timer;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Structure {

    public static List<Structure> structureList = new ArrayList<>();

    private double drawLuck;
    private double drawOutOf;

    private String name;
    private String world;
    private Biome[] biomes;
    private Material[] whitelistedBlocks;

    private File schematic;
    private Clipboard clipboard;

    private int offsetX = 0, offsetY = 0, offsetZ = 0;
    private int minHeight, maxHeight;
    private boolean placeAir = true, randomRotation = true, spawnInWater = false, spawnInLava = false;
    private boolean smartPast = false;

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
}
