package fr.zelytra.novaStructura.events;

import fr.zelytra.novaStructura.manager.structure.Structure;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;
import org.jetbrains.annotations.NotNull;

public class OnNewChunk implements Listener {

    private final static int CHUNK_SIZE = 16, Y_MAX = Bukkit.getWorld("world").getMaxHeight(), Y_MIN = Bukkit.getWorld("world").getMinHeight();

    @EventHandler
    public void onNewChunk(ChunkLoadEvent e) {
        if (!e.isNewChunk()) return;

        for (Structure structure : Structure.structureList) {

            if (!structure.draw()) {
                continue;
            }

            Location spawn = getRandomLocInChunk(e.getChunk(), structure);
            if (spawn != null && structure.spawnChecker(spawn)){}
                structure.paste(spawn);
        }


    }

    private Location getRandomLocInChunk(@NotNull Chunk chunk, Structure structure) {

        int randomX = (int) (CHUNK_SIZE * Math.random());
        int randomZ = (int) (CHUNK_SIZE * Math.random());
        int highestY = 0;
        boolean foundSpot = false;

        if (structure.isSpawnInCave()) {

            for (int y = Math.max(structure.getMinHeight(), Y_MIN); y <= Math.min(structure.getMaxHeight(), Y_MAX); y++) {

                Block block = chunk.getBlock(randomX, Math.min(y + 1,Y_MAX), randomZ);
                if (block.getType() == Material.AIR) {
                    highestY = y;
                    foundSpot = true;
                    break;
                }


            }


        } else if (structure.isSpawnInLava()) {

            for (int y = Math.max(structure.getMinHeight(), Y_MIN); y <= Math.min(structure.getMaxHeight(), Y_MAX); y++) {

                Block block = chunk.getBlock(randomX, Math.min(y + 1,Y_MAX), randomZ);
                if (block.getType() == Material.LAVA) {
                    highestY = y;
                    foundSpot = true;
                    break;
                }


            }

        } else if (structure.isSpawnInWater()) {

            for (int y = Math.max(structure.getMinHeight(), Y_MIN); y <= Math.min(structure.getMaxHeight(), Y_MAX); y++) {

                Block block = chunk.getBlock(randomX, Math.min(y + 1,Y_MAX), randomZ);
                if (block.getType() == Material.WATER) {
                    highestY = y;
                    foundSpot = true;
                    break;
                }


            }

        } else {
            for (int y = Y_MAX; y >= Y_MIN; y--) {
                Block block = chunk.getBlock(randomX, y - 1, randomZ);
                if (block.getType() != Material.AIR) {
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
