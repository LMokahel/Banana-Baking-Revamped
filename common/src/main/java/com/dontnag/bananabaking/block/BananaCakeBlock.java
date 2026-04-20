package com.dontnag.bananabaking.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.CakeBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;

public class BananaCakeBlock extends CakeBlock {

    public BananaCakeBlock(Properties properties) {
        super(properties);
    }

    @NotNull
    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if(player.canEat(false)){
            player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 40, 2));
        }
        return super.useWithoutItem(state, level, pos, player, hitResult);
    }


}
