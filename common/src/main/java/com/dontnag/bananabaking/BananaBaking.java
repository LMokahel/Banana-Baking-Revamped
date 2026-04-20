package com.dontnag.bananabaking;

import com.dontnag.bananabaking.block.BananaBlocks;
import com.dontnag.bananabaking.block.entity.BananaBlockEntities;
import com.dontnag.bananabaking.item.BananaItems;
import com.dontnag.bananabaking.recipe.BananaRecipeTypes;
import com.dontnag.bananabaking.menus.BananaMenuTypes;
import net.blay09.mods.balm.api.Balm;
import net.minecraft.resources.ResourceLocation;
import net.blay09.mods.balm.core.BalmRegistrars;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BananaBaking {

    public static final Logger logger = LoggerFactory.getLogger(BananaBaking.class);

    public static final String MOD_ID = "bananabaking";

    public static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }

    public static BananaBakingConfig config() {
        return Balm.getConfig().getActiveConfig(BananaBakingConfig.class);
    }

    public static void initialize(BalmRegistrars registrars) {
        Balm.getConfig().registerConfig(BananaBakingConfig.class);

        registrars.blocks(BananaBlocks::initialize);
        registrars.items(BananaItems::initialize);
        registrars.creativeModeTabs(BananaItems::initialize);
        registrars.blockEntityTypes(BananaBlockEntities::initialize);
        registrars.recipeTypes(BananaRecipeTypes::initialize);
        registrars.menuTypes(BananaMenuTypes::initialize);
    }

}
