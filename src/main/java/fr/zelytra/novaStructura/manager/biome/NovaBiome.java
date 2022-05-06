package fr.zelytra.novaStructura.manager.biome;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.chunk.ChunkAccess;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Biome;
import org.bukkit.craftbukkit.v1_18_R2.CraftWorld;

public class NovaBiome {

    private final Biome vanillaBiome;
    private final NamespacedKey biomeKey;

    public NovaBiome(Location location) {
        this.vanillaBiome = location.getBlock().getBiome();

        if (vanillaBiome == Biome.CUSTOM)
            biomeKey = getCustomBiomesKey(location);
        else
            biomeKey = vanillaBiome.getKey();
    }

    public NovaBiome(NamespacedKey key) {
        vanillaBiome = Biome.CUSTOM;
        biomeKey = key;
    }

    public NovaBiome(Biome biome) {
        vanillaBiome = biome;
        biomeKey = new NamespacedKey(biome.getKey().value(), biome.name());
    }

    public static boolean match(NovaBiome biome, NovaBiome actualBiome) {
        return biome.biomeKey.namespace().equalsIgnoreCase(actualBiome.biomeKey.namespace()) && biome.biomeKey.value().equalsIgnoreCase(actualBiome.biomeKey.value());
    }

    public NamespacedKey getCustomBiomesKey(Location location) {
        // NMS position
        BlockPos pos = new BlockPos(location.getBlockX(), location.getY(), location.getBlockZ());

        // NMS chunk from pos
        ChunkAccess nmsChunk = ((CraftWorld) location.getWorld()).getHandle().getChunk(pos);

        if (nmsChunk != null) {
            String key = nmsChunk.getNoiseBiome(pos.getX(), pos.getY(), pos.getZ()).unwrap().left().get().location().getNamespace();
            String value = nmsChunk.getNoiseBiome(pos.getX(), pos.getY(), pos.getZ()).unwrap().left().get().location().getPath();

            return new NamespacedKey(key, value);
        }
        return null;
    }

    public NamespacedKey getBiomeKey() {
        return biomeKey;
    }

    @Override
    public String toString() {
        return "Key: " + biomeKey.namespace() + " | Value: " + biomeKey.value();
    }
}
