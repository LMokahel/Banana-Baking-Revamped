package com.dontnag.bananabaking.neoforge;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.neoforge.NeoForgeLoadContext;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import com.dontnag.bananabaking.BananaBaking;

@Mod(BananaBaking.MOD_ID)
public class NeoForgeBananaBaking {

    public NeoForgeBananaBaking(IEventBus modEventBus) {
        final var context = new NeoForgeLoadContext(modEventBus);
        Balm.initializeMod(BananaBaking.MOD_ID, context, BananaBaking::initialize);
    }
}
