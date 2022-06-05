package fr.zelytra.novaStructura.utils;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.TagParser;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_18_R2.CraftWorld;

public class BlockEntityNBT {

    public static String getNBTTag(Block block) {
        CraftWorld cw = (CraftWorld) block.getWorld();
        BlockPos bp = new BlockPos(block.getX(),block.getY(),block.getZ());
        BlockEntity be = cw.getHandle().getBlockEntity(bp);

        return (be != null) ? be.saveWithoutMetadata().getAsString() : null;
    }

    public static void setNBTTag(Block block, String nbt) throws CommandSyntaxException {
        CraftWorld cw = (CraftWorld) block.getWorld();
        BlockPos bp = new BlockPos(block.getX(),block.getY(),block.getZ());
        BlockEntity be = cw.getHandle().getBlockEntity(bp);
        be.load(TagParser.parseTag(nbt));
    }


}
