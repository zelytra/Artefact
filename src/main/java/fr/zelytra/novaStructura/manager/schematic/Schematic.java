package fr.zelytra.novaStructura.manager.schematic;

import fr.zelytra.novaStructura.NovaStructura;
import fr.zelytra.novaStructura.manager.schematic.selector.Selector;
import fr.zelytra.novaStructura.manager.schematic.wordload.SetBlock;
import fr.zelytra.novaStructura.manager.schematic.wordload.WorkLoad;
import fr.zelytra.novaStructura.manager.structure.StructureFolder;
import fr.zelytra.novaStructura.manager.structure.StructureManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Schematic implements Serializable {


    private List<MaterialMap> materialMaps;
    private int[][][] blockMap;
    private String name;

    public Schematic(Selector selection, String name) {

        this.name = name;
        materialMaps = new ArrayList<>();
        scanSelection(selection);

    }

    public void paste(Location location) {
        NovaStructura.workloadThread.addAllLoad(generateWorkLoad(location));
    }

    private List<WorkLoad> generateWorkLoad(Location location) {
        List<WorkLoad> setBlockList = new ArrayList<>();

        for (int x = 0; x < blockMap.length; x++) {
            for (int y = 0; y < blockMap[x].length; y++) {
                for (int z = 0; z < blockMap[x][y].length; z++) {

                    setBlockList.add(new SetBlock(location.getWorld(),
                            location.getBlockX() + x,
                            location.getBlockY() + y,
                            location.getBlockZ() + z,
                            Material.getMaterial(materialMaps.get(blockMap[x][y][z]).materialName())));

                }
            }
        }
        return setBlockList;
    }

    private void scanSelection(Selector selection) {
        int minX = Math.min(selection.corner1.getBlockX(), selection.corner2.getBlockX());
        int minY = Math.min(selection.corner1.getBlockY(), selection.corner2.getBlockY());
        int minZ = Math.min(selection.corner1.getBlockZ(), selection.corner2.getBlockZ());

        int maxX = Math.max(selection.corner1.getBlockX(), selection.corner2.getBlockX());
        int maxY = Math.max(selection.corner1.getBlockY(), selection.corner2.getBlockY());
        int maxZ = Math.max(selection.corner1.getBlockZ(), selection.corner2.getBlockZ());

        blockMap = new int[maxX - minX + 1][maxY - minY + 1][maxZ - minZ + 1];
        Bukkit.getScheduler().runTaskAsynchronously(NovaStructura.getInstance(), () -> {
            for (int x = minX; x <= maxX; x++) {
                for (int y = minY; y <= maxY; y++) {
                    for (int z = minZ; z <= maxZ; z++) {

                        Material type = selection.corner1.getWorld().getBlockAt(x, y, z).getType();
                        blockMap[x - minX][y - minY][z - minZ] = getMaterialId(type);
                        //NovaStructura.log("x: " + (x - minX) + " y: " + (y - minY) + " z: " + (z - minZ) + " id: " + blockMap[x - minX][y - minY][z - minZ] + " type: " + type.name(), LogType.INFO);


                    }
                }
            }
        });

    }

    private int getMaterialId(Material material) {
        for (MaterialMap materialMap : materialMaps) {
            if (materialMap.materialName().equalsIgnoreCase(material.name())) {
                return materialMap.id();
            }
        }
        int id = 0;
        if (materialMaps.size() != 0)
            id = materialMaps.size();

        materialMaps.add(new MaterialMap(material.name(), id));
        return id;

    }

    public void save() {

        try {

            File schematic = new File(StructureManager.PATH + StructureFolder.SCHEMATIC.folderName + File.separator + name + StructureFolder.SCHEMATIC.extension);
            schematic.createNewFile();
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(schematic));
            oos.writeObject(this);
            oos.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static Schematic loadFromFile(File schematic) {
        try {

            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(schematic));
            Schematic reader = (Schematic) ois.readObject();
            ois.close();
            return reader;

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void delete() {

        File schematic = new File(StructureManager.PATH + StructureFolder.SCHEMATIC.folderName + File.separator + name + StructureFolder.SCHEMATIC.extension);
        schematic.delete();

    }
}
