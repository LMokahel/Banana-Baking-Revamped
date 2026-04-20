package com.dontnag.bananabaking.fabric;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.EmptyLoadContext;
import net.fabricmc.api.ModInitializer;
import com.dontnag.bananabaking.BananaBaking;

public class FabricBananaBaking implements ModInitializer {

    @Override
    public void onInitialize() {
        Balm.initializeMod(BananaBaking.MOD_ID, EmptyLoadContext.INSTANCE, BananaBaking::initialize);
    }
}
