package fr.zelytra.novaStructura.events;

import fr.zelytra.novaStructura.manager.structure.Structure;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;
import org.jetbrains.annotations.NotNull;

public class OnNewChunk implements Listener {
    private final static int chunkSize = 16;

    @EventHandler
    public void onNewChunk(ChunkLoadEvent e) {
        if (!e.isNewChunk()) return;

        for (Structure structure : Structure.structureList)
            if (structure.naturalDraw(e.getChunk()))
                structure.paste(getRandomLocInChunk(e.getChunk()));


    }

    private @NotNull Location getRandomLocInChunk(@NotNull Chunk chunk) {
        int randomX = (int) ((chunk.getX() * chunkSize) + (chunkSize * Math.random()));
        int randomZ = (int) ((chunk.getZ() * chunkSize) + (chunkSize * Math.random()));
        int highestY = chunk.getWorld().getHighestBlockYAt(randomX, randomZ);

        return new Location(chunk.getWorld(), randomX, highestY, randomZ);
    }
}
