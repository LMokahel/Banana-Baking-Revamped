package com.dontnag.bananabaking.fabric.client;

import com.dontnag.bananabaking.block.BananaBlocks;
import net.blay09.mods.balm.api.client.BalmClient;
import net.blay09.mods.balm.api.EmptyLoadContext;
import net.fabricmc.api.ClientModInitializer;
import com.dontnag.bananabaking.BananaBaking;
import com.dontnag.bananabaking.client.BananaBakingClient;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.renderer.RenderType;

public class FabricBananaBakingClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        BalmClient.initializeMod(BananaBaking.MOD_ID, EmptyLoadContext.INSTANCE, BananaBakingClient::initialize);
        BlockRenderLayerMap.INSTANCE.putBlock(BananaBlocks.VANILLA_CROP.asBlock(), RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(BananaBlocks.BANANA_CROP.asBlock(), RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(BananaBlocks.MIXING_BOWL.asBlock(), RenderType.cutout());
    }
}
