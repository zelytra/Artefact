package fr.zelytra.novaStructura.manager.schematic;

import org.bukkit.Bukkit;
import org.bukkit.block.BlockState;
import org.bukkit.block.CommandBlock;
import org.bukkit.block.TileState;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Directional;

import java.io.Serializable;

public class SchematicBlock implements Serializable {

    private String data;
    private int materialId;
    private transient BlockData blockData;

    public SchematicBlock(int materialId,BlockData data) {
        this(materialId,data,null);
    }

    public SchematicBlock(int materialId, BlockData data, String command) {
        this.materialId = materialId;
        if (command != null) Bukkit.broadcastMessage(command);

        if (data instanceof Directional) {
            this.data = data.getAsString();
            this.blockData = data;
        }

    }

    public boolean hasData() {
        return data != null;
    }

    public int getMaterialId() {
        return materialId;
    }

    public BlockData getBlockData() {
        if (blockData == null) this.blockData = Bukkit.createBlockData(data);
        return blockData;
    }
}
