package com.dontnag.bananabaking.client;

import net.blay09.mods.balm.client.BalmClientRegistrars;

public class BananaBakingClient {

    public static void initialize(BalmClientRegistrars registrars) {
        registrars.menuScreens(BananaScreens::initialize);
    }
}
