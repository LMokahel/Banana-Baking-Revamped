package com.dontnag.bananabaking.client;

import com.dontnag.bananabaking.client.gui.screen.BakingOvenScreen;
import com.dontnag.bananabaking.client.gui.screen.MixingBowlScreen;
import com.dontnag.bananabaking.menus.BananaMenuTypes;
import net.blay09.mods.balm.client.gui.screens.inventory.BalmMenuScreenRegistrar;

public class BananaScreens {

    public static void initialize(BalmMenuScreenRegistrar menuScreens) {
        menuScreens.register(BananaMenuTypes.BAKING_MENU, BakingOvenScreen::new);
        menuScreens.register(BananaMenuTypes.MIXING_MENU, MixingBowlScreen::new);
    }
}
