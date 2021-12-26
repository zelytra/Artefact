package fr.zelytra.novaStructura.manager.schematic;

import fr.zelytra.novaStructura.NovaStructura;
import fr.zelytra.novaStructura.manager.schematic.selector.Selector;
import fr.zelytra.novaStructura.manager.schematic.workload.SetBlock;
import fr.zelytra.novaStructura.manager.schematic.workload.WorkLoad;
import fr.zelytra.novaStructura.manager.structure.Structure;
import fr.zelytra.novaStructura.manager.structure.StructureFolder;
import fr.zelytra.novaStructura.manager.structure.StructureManager;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Schematic implements Serializable {


    private final List<MaterialMap> materialMaps;
    private SchematicBlock[][][] blockMap;
    private final String name;

    public Schematic(Selector selection, String name) {

        this.name = name;
        materialMaps = new ArrayList<>();
        scanSelection(selection);

    }

    public void paste(Location location, Structure structure) {
        NovaStructura.workloadThread.addAllLoad(generateWorkLoad(location, structure));
    }

    private List<WorkLoad> generateWorkLoad(Location location, Structure structure) {
        List<WorkLoad> setBlockList = new ArrayList<>();

        for (int x = 0; x < blockMap.length; x++) {
            for (int y = 0; y < blockMap[x].length; y++) {
                for (int z = 0; z < blockMap[x][y].length; z++) {

                    Material material = Material.getMaterial(materialMaps.get(blockMap[x][y][z].getMaterialId()).materialName());

                    if (structure.isPlaceAir() && material == Material.AIR) continue;
                    ;
                    if (structure.getLootTable() != null && structure.getLootContainer().contains(material))
                        setBlockList.add(new SetBlock(location.getWorld(),
                                location.getBlockX() + x,
                                location.getBlockY() + y,
                                location.getBlockZ() + z,
                                material,
                                (blockMap[x][y][z].hasData() ? blockMap[x][y][z].getBlockData() : null), structure.drawLoot()));

                    else
                        setBlockList.add(new SetBlock(location.getWorld(),
                                location.getBlockX() + x,
                                location.getBlockY() + y,
                                location.getBlockZ() + z,
                                material,
                                (blockMap[x][y][z].hasData() ? blockMap[x][y][z].getBlockData() : null)));

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

        blockMap = new SchematicBlock[maxX - minX + 1][maxY - minY + 1][maxZ - minZ + 1];

        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                for (int z = minZ; z <= maxZ; z++) {
                    Block block = selection.corner1.getWorld().getBlockAt(x, y, z);
                    blockMap[x - minX][y - minY][z - minZ] = new SchematicBlock(getMaterialId(block.getType()), block.getBlockData());
                }
            }
        }


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
