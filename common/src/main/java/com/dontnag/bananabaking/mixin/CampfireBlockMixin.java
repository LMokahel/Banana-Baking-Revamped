package com.dontnag.bananabaking.mixin;

import com.dontnag.bananabaking.block.BananaBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.CampfireBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CampfireBlock.class)
public class CampfireBlockMixin {

    @Inject(method = "makeParticles", at = @At("HEAD"), cancellable = true)
    private static void makeParticles(Level level, BlockPos pos, boolean isSignalFire, boolean spawnExtraSmoke, CallbackInfo ci){
        if(level.getBlockState(pos.above()).is(BananaBlocks.BAKING_OVEN)){
            ci.cancel();
        }
    }
}
