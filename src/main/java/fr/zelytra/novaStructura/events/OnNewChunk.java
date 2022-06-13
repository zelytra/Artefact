package fr.zelytra.novaStructura.events;

import fr.zelytra.novaStructura.manager.structure.Structure;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;
import org.jetbrains.annotations.NotNull;

public class OnNewChunk implements Listener {

    private final static int CHUNK_SIZE = 16; //Y_MAX = Bukkit.getWorld("world").getMaxHeight(), Y_MIN = Bukkit.getWorld("world").getMinHeight();

    @EventHandler
    public void onNewChunk(ChunkLoadEvent e) {
        if (!e.isNewChunk()) return;

        for (Structure structure : Structure.structureList) {

            if (!structure.draw()) {
                continue;
            }

            Location spawn = getRandomLocInChunk(e.getChunk(), structure);
            if (spawn != null && structure.spawnChecker(spawn)){
                structure.paste(spawn);
            }
        }


    }

    private Location getRandomLocInChunk(@NotNull Chunk chunk, Structure structure) {

        int randomX = (int) (CHUNK_SIZE * Math.random());
        int randomZ = (int) (CHUNK_SIZE * Math.random());
        int highestY = 0;
        boolean foundSpot = false;
        int ymax = chunk.getWorld().getMaxHeight(), ymin = chunk.getWorld().getMinHeight();

        if (structure.isSpawnInCave()) {

            for (int y = Math.max(structure.getMinHeight(), ymin); y <= Math.min(structure.getMaxHeight(), ymax); y++) {

                Block block = chunk.getBlock(randomX, Math.min(y + 1,ymax), randomZ);
                if (block.getType() == Material.AIR) {
                    highestY = y;
                    foundSpot = true;
                    break;
                }


            }


        } else {
            for (int y = ymax; y > ymin; y--) {
                Block block = chunk.getBlock(randomX, y - 1, randomZ);
                boolean isAir = (block.getType() == Material.AIR || block.isBurnable()), isWater = (block.getType() == Material.WATER && structure.isSpawnInWater()), isLava = (block.getType() == Material.LAVA && structure.isSpawnInLava());
                if (!isAir) {
                    if (isWater || isLava) {
                        continue;
                    }

                    
                    foundSpot = true;
                    highestY = y;
                    break;
                }
            }
        }

        if (!foundSpot) return null;


        return new Location(chunk.getWorld(), (chunk.getX() * CHUNK_SIZE) + randomX, highestY, (chunk.getZ() * CHUNK_SIZE) + randomZ);
    }
}
