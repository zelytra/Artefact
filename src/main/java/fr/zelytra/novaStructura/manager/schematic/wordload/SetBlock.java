package fr.zelytra.novaStructura.manager.schematic.wordload;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

public record SetBlock(World world, int x, int y, int z, Material material,String data) implements WorkLoad {

    @Override
    public void compute() {
        Block block = world.getBlockAt(x, y, z);
        block.setType(material);
        block.setBlockData(Bukkit.createBlockData(data));
    }
}
