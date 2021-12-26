package fr.zelytra.novaStructura.manager.schematic.workload;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Container;
import org.bukkit.block.data.BlockData;
import org.bukkit.inventory.ItemStack;

public class SetBlock implements WorkLoad {

    private final int x, y, z;
    private final World world;
    private final Material material;
    private final BlockData data;
    private ItemStack[] content;

    public SetBlock(World world, int x, int y, int z, Material material, BlockData data) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.world = world;
        this.material = material;
        this.data = data;
    }

    public SetBlock(World world, int x, int y, int z, Material material, BlockData data, ItemStack[] content) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.world = world;
        this.material = material;
        this.data = data;
        this.content = content;
    }

    @Override
    public void compute() {
        Block block = world.getBlockAt(x, y, z);
        block.setType(material);

        if (data != null)
            block.setBlockData(data);

        if (content != null && block.getState() instanceof Container) {
            BlockState state = block.getState();
            ((Container)state).getInventory().setContents(content);
        }


    }
}


