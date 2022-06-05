package fr.zelytra.novaStructura.manager.schematic;

import net.minecraft.nbt.CompoundTag;
import org.bukkit.Bukkit;
import org.bukkit.block.BlockState;
import org.bukkit.block.CommandBlock;
import org.bukkit.block.TileState;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Directional;

import java.io.Serializable;

public class SchematicBlock implements Serializable {

    private String data;
    private String nbt;
    private int materialId;
    private transient BlockData blockData;

    public SchematicBlock(int materialId,BlockData data) {
        this(materialId,data,null);
    }

    public SchematicBlock(int materialId, BlockData data, String nbt) {
        this.materialId = materialId;

        if (nbt != null) {
            this.nbt = nbt;
        }

        this.data = data.getAsString();
        this.blockData = data;

    }

    public boolean hasData() {
        return data != null;
    }

    public boolean hasNBT() {
        return nbt != null;
    }

    public int getMaterialId() {
        return materialId;
    }

    public BlockData getBlockData() {
        if (blockData == null) this.blockData = Bukkit.createBlockData(data);
        return blockData;
    }

    public String getNBT() {
        return nbt;
    }
}
