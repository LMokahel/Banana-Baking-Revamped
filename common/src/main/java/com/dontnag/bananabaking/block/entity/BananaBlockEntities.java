package com.dontnag.bananabaking.block.entity;

import com.dontnag.bananabaking.block.BananaBlocks;
import net.blay09.mods.balm.world.level.block.entity.BalmBlockEntityTypeRegistrar;
import net.minecraft.core.Holder;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class BananaBlockEntities {

    public static Holder<BlockEntityType<BakingOvenEntity>> BAKING_OVEN;
    public static Holder<BlockEntityType<MixingBowlBlockEntity>> MIXING_BOWL;

    public static void initialize(BalmBlockEntityTypeRegistrar blockEntities){
        BAKING_OVEN = blockEntities.register("baking_oven", BakingOvenEntity::new, BananaBlocks.BAKING_OVEN).asHolder();
        MIXING_BOWL = blockEntities.register("mixing_bowl", MixingBowlBlockEntity::new, BananaBlocks.MIXING_BOWL).asHolder();
    }
}
