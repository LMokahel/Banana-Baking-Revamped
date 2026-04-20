package com.dontnag.bananabaking.neoforge.client;

import net.blay09.mods.balm.api.client.BalmClient;
import net.blay09.mods.balm.neoforge.NeoForgeLoadContext;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import com.dontnag.bananabaking.BananaBaking;
import com.dontnag.bananabaking.client.BananaBakingClient;

@Mod(value = BananaBaking.MOD_ID, dist = Dist.CLIENT)
public class NeoForgeBananaBakingClient {

    public NeoForgeBananaBakingClient(IEventBus modEventBus) {
        final var context = new NeoForgeLoadContext(modEventBus);
        BalmClient.initializeMod(BananaBaking.MOD_ID, context, BananaBakingClient::initialize);
    }
}
