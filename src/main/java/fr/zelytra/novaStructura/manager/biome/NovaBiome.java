package fr.zelytra.novaStructura.manager.biome;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.chunk.ChunkAccess;
import org.bukkit.Location;
import org.bukkit.block.Biome;
import org.bukkit.craftbukkit.v1_18_R1.CraftWorld;

public class NovaBiome {

    private final Biome vanillaBiome;
    private final String biomeKey;

    public NovaBiome(Location location) {
        this.vanillaBiome = location.getBlock().getBiome();

        if (vanillaBiome == Biome.CUSTOM)
            biomeKey = getBiomeBase(location).getBiomeCategory().getName();
        else
            biomeKey = vanillaBiome.getKey().getKey();


    }

    private String getCustomBiomesKey(Location location) {
        return biomeKey;
    }

    public net.minecraft.world.level.biome.Biome getBiomeBase(Location location) {
        // NMS position
        BlockPos pos = new BlockPos(location.getBlockX(), 0, location.getBlockZ());

        // NMS chunk from pos
        ChunkAccess nmsChunk = ((CraftWorld) location.getWorld()).getHandle().getChunk(pos);

        if (nmsChunk != null) {
            return nmsChunk.getNoiseBiome(pos.getX(), 0, pos.getZ());
        }
        return null;
    }

    public String getBiomeKey() {
        return biomeKey;
    }
}
