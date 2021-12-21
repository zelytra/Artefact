package fr.zelytra.novaStructura.manager.schematic.wordload;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;

public class SetBlock implements WorkLoad {

    private final int x, y, z;
    private final World world;
    private final Material material;
    private final BlockData data;

    public SetBlock(World world, int x, int y, int z, Material material, BlockData data) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.world = world;
        this.material = material;
        this.data = data;
    }

    @Override
    public void compute() {
        Block block = world.getBlockAt(x, y, z);
        block.setType(material);
        if (data != null)
            block.setBlockData(data);
    }
}


