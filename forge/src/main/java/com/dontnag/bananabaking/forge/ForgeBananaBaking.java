package com.dontnag.bananabaking.forge;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.client.BalmClient;
import net.blay09.mods.balm.forge.ForgeLoadContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import com.dontnag.bananabaking.BananaBaking;
import com.dontnag.bananabaking.client.BananaBakingClient;

@Mod(BananaBaking.MOD_ID)
public class ForgeBananaBaking {

    public ForgeBananaBaking(FMLJavaModLoadingContext context) {
        final var loadContext = new ForgeLoadContext(context.getModEventBus());
        Balm.initializeMod(BananaBaking.MOD_ID, loadContext, BananaBaking::initialize);
        if (FMLEnvironment.dist.isClient()) {
            BalmClient.initializeMod(BananaBaking.MOD_ID, loadContext, BananaBakingClient::initialize);
        }
    }
}
