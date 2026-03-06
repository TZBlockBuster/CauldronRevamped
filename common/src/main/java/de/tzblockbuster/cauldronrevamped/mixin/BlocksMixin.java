package de.tzblockbuster.cauldronrevamped.mixin;

import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LayeredCauldronBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Slice;

import java.util.function.Function;

@Mixin(Blocks.class)
public class BlocksMixin {

    @ModifyArg(method = "<clinit>", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/Blocks;register(Ljava/lang/String;Ljava/util/function/Function;Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;)Lnet/minecraft/world/level/block/Block;"), slice = @Slice(from = @At(value = "FIELD", target = "Lnet/minecraft/world/level/block/Blocks;WATER_CAULDRON:Lnet/minecraft/world/level/block/Block;", opcode = Opcodes.PUTSTATIC), to = @At(value = "FIELD", target = "Lnet/minecraft/world/level/block/Blocks;POWDER_SNOW_CAULDRON:Lnet/minecraft/world/level/block/Block;", opcode = Opcodes.PUTSTATIC)), index = 1)
    private static Function<BlockBehaviour.Properties, Block> cauldronrevamped$cauldron(Function<BlockBehaviour.Properties, Block> function) {
        return properties -> new LayeredCauldronBlock(Biome.Precipitation.NONE, CauldronInteraction.LAVA, properties);
    }
}
