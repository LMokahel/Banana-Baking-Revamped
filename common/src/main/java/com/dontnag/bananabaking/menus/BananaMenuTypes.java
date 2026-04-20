package com.dontnag.bananabaking.menus;

import com.dontnag.bananabaking.block.entity.ContainerBlockEntity;
import com.dontnag.bananabaking.block.entity.CookingBlockEntity;
import net.blay09.mods.balm.api.menu.BalmMenuFactory;
import net.blay09.mods.balm.world.inventory.BalmMenuTypeRegistrar;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.block.entity.BlockEntity;

public class BananaMenuTypes {

    private static BalmMenuTypeRegistrar registrar;

    public static Holder<MenuType<BakingOvenMenu>> BAKING_MENU;
    public static Holder<MenuType<BakingOvenMenu>> BLENDING_MENU;
    public static Holder<MenuType<BakingOvenMenu>> FRYING_MENU;
    public static Holder<MenuType<MixingBowlMenu>> MIXING_MENU;
    public static Holder<MenuType<BakingOvenMenu>> FREEZING_MENU;
    public static Holder<MenuType<BakingOvenMenu>> PRESSING_MENU;

    public static void initialize(BalmMenuTypeRegistrar menuTypes) {
        registrar = menuTypes;
        BAKING_MENU = register("baking_menu");
        MIXING_MENU = register("mixing_menu");
    }

    private static <T extends ContainerMenu<?>> Holder<MenuType<T>> register(String name){
        return registrar.register(name, new BalmMenuFactory<T, BlockPos>() {

            @SuppressWarnings("unchecked")
            @Override
            public T create(int windowId, Inventory inventory, BlockPos pos) {
                BlockEntity blockEntity = inventory.player.level().getBlockEntity(pos);
                return ((ContainerBlockEntity<T>) blockEntity).createMenu(windowId, inventory, inventory.player);
            }

            @Override
            public StreamCodec<RegistryFriendlyByteBuf, BlockPos> getStreamCodec() {
                return BlockPos.STREAM_CODEC.cast();
            }
        }).asHolder();
    }
}
