package fr.zelytra.novaStructura.events;

import fr.zelytra.novaStructura.manager.structure.Structure;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;
import org.jetbrains.annotations.NotNull;

public class OnNewChunk implements Listener {

    private final static int chunkSize = 16;

    @EventHandler
    public void onNewChunk(ChunkLoadEvent e) {
        if (!e.isNewChunk()) return;
        for (Structure structure : Structure.structureList) {

            Location spawn = getRandomLocInChunk(e.getChunk(), structure);

            if (spawn != null && structure.naturalDraw(spawn))
                structure.paste(spawn);
        }


    }

    private Location getRandomLocInChunk(@NotNull Chunk chunk, Structure structure) {

        int randomX = (int) (chunkSize * Math.random());
        int randomZ = (int) (chunkSize * Math.random());
        int highestY = 0;
        boolean foundSpot = false;

        if (structure.isSpawnInCave()) {

            for (int y = structure.getMinHeight(); y <= structure.getMaxHeight(); y++) {

                Block block = chunk.getBlock(randomX, y + 1, randomZ);
                if (block.getType() == Material.AIR) {
                    highestY = y;
                    foundSpot = true;
                    break;
                }


            }


        } else if (structure.isSpawnInLava()) {

            for (int y = structure.getMinHeight(); y <= structure.getMaxHeight(); y++) {

                Block block = chunk.getBlock(randomX, y + 1, randomZ);
                if (block.getType() == Material.LAVA) {
                    highestY = y;
                    foundSpot = true;
                    break;
                }


            }

        } else if (structure.isSpawnInWater()) {

            for (int y = structure.getMinHeight(); y <= structure.getMaxHeight(); y++) {

                Block block = chunk.getBlock(randomX, y + 1, randomZ);
                if (block.getType() == Material.WATER) {
                    highestY = y;
                    foundSpot = true;
                    break;
                }


            }

        } else {
            highestY = chunk.getWorld().getHighestBlockYAt(randomX, randomZ);
            foundSpot = true;
        }

        if (!foundSpot) return null;


        return new Location(chunk.getWorld(), (chunk.getX() * chunkSize) + randomX, highestY, (chunk.getZ() * chunkSize) + randomZ);
    }
}
