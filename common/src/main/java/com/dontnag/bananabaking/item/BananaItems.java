package com.dontnag.bananabaking.item;

import com.dontnag.bananabaking.BananaBaking;

import com.dontnag.bananabaking.block.BananaBlocks;
import net.blay09.mods.balm.world.item.BalmCreativeModeTabRegistrar;
import net.blay09.mods.balm.world.item.BalmItemRegistrar;
import net.blay09.mods.balm.world.item.DeferredItem;

import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemNameBlockItem;
import net.minecraft.world.item.Items;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;

public class BananaItems {

    private static BalmItemRegistrar registrar;
    public static final Set<DeferredItem> ITEMS = new HashSet<>();

    public static DeferredItem BANANA;
    public static DeferredItem BANANA_BUNCH;
    public static DeferredItem BANANA_BREAD;
    public static DeferredItem BANANA_PIE;
    public static DeferredItem BANANA_PIE_SLICE;
    public static DeferredItem BANANA_FRIED;
    public static DeferredItem BANANA_DONUT;
    public static DeferredItem BANANA_CHOCOLATE;
    public static DeferredItem BANANA_COOKIE;
    public static DeferredItem BANANA_OILED;
    public static DeferredItem BANANA_PUDDING;
    public static DeferredItem BANANA_SMOOTHIE;
    public static DeferredItem BANANA_SEEDS;
    public static DeferredItem VANILLA_BEANS;
    public static DeferredItem VANILLA_EXTRACT;
    public static DeferredItem VANILLA_BEAN_SEEDS;
    public static DeferredItem MIXING_SPOON;

    public static void initialize(BalmItemRegistrar items) {
        registrar = items;
        BANANA = register("banana", food(1, 0.5f));
        BANANA_BUNCH = register("banana_bunch");
        BANANA_BREAD = register("banana_bread", food(10, 0.7f));
        BANANA_PIE = register("banana_pie", food(12, 0.2f));
        BANANA_PIE_SLICE = register("banana_pie_slice", food(3, 0.2f));
        BANANA_FRIED = register("banana_fried", food(7, 0.4f));
        BANANA_DONUT = register("banana_donut", food(14, 0.2f));
        BANANA_CHOCOLATE = register("banana_chocolate", food(7, 0.4f, Items.STICK));
        BANANA_COOKIE = register("banana_cookie", food(2, 0.25f, MobEffects.REGENERATION, 80, 1));
        BANANA_OILED = register("banana_oiled", food(6, 0.35f, MobEffects.HUNGER, 1200, 0));
        BANANA_PUDDING = register("banana_pudding", food(8, 0.5f, MobEffects.MOVEMENT_SPEED, 600, 1, Items.BOWL), 16);
        BANANA_SMOOTHIE = register("banana_smoothie", food(1, 0.1f, MobEffects.MOVEMENT_SPEED, 300, 1, Items.GLASS_BOTTLE), 16);
        BANANA_SEEDS = register("banana_seeds", p ->
            new ItemNameBlockItem(com.dontnag.bananabaking.block.BananaBlocks.BANANA_CROP.asBlock(), p)
        );
        VANILLA_BEANS = register("vanilla_beans");
        VANILLA_EXTRACT = register("vanilla_extract", food(1, 0.5f, MobEffects.HUNGER, 1200, 0, Items.GLASS_BOTTLE), 16, Items.GLASS_BOTTLE);
        VANILLA_BEAN_SEEDS = register("vanilla_bean_seeds", p ->
            new ItemNameBlockItem(com.dontnag.bananabaking.block.BananaBlocks.VANILLA_CROP.asBlock(), p)
        );
        MIXING_SPOON = register("mixing_spoon");
    }

    private static MobEffectInstance effect(Holder<MobEffect> effect, int dur, int amp){
        return new MobEffectInstance(effect, dur, amp);
    }

    private static FoodProperties food(int nutrition, float saturation){
        return new FoodProperties.Builder()
            .nutrition(nutrition)
            .saturationModifier(saturation)
            .build();
    }

    private static FoodProperties food(int nutrition, float saturation, Item leftOver){
        return new FoodProperties.Builder()
            .nutrition(nutrition)
            .saturationModifier(saturation)
            .usingConvertsTo(leftOver)
            .build();
    }

    private static FoodProperties food(int nutrition, float saturation, Holder<MobEffect> effectType, int dur, int amp){
        return new FoodProperties.Builder()
            .nutrition(nutrition)
            .saturationModifier(saturation)
            .effect(effect(effectType, dur, amp), 1f)
            .build();
    }

    private static FoodProperties food(int nutrition, float saturation, Holder<MobEffect> effectType, int dur, int amp, Item leftOver){
        return new FoodProperties.Builder()
            .nutrition(nutrition)
            .saturationModifier(saturation)
            .effect(effect(effectType, dur, amp), 1f)
            .usingConvertsTo(leftOver)
            .build();
    }

    private static DeferredItem register(String name, FoodProperties food){
        return register(name, food, 64);
    }

    private static DeferredItem register(String name, FoodProperties food, int stackSize){
        return register(name, food, stackSize, Items.AIR);
    }

    private static DeferredItem register(String name, FoodProperties food, int stackSize, Item remainder){
        return register(name, new Item.Properties().food(food).stacksTo(stackSize).craftRemainder(remainder), Item::new);
    }

    private static DeferredItem register(String name){
        return register(name, new Item.Properties(), Item::new);
    }

    private static DeferredItem register(String name, Function<Item.Properties, Item> constructor){
        return register(name, new Item.Properties(), constructor);
    }

    private static DeferredItem register(String name, Item.Properties properties, Function<Item.Properties, Item> constructor){
        DeferredItem item = registrar.register(name, p -> constructor.apply(properties)).asDeferredItem();
        ITEMS.add(item);
        return item;
    }

    public static void initialize(BalmCreativeModeTabRegistrar creativeModeTabs) {
        creativeModeTabs.register(BananaBaking.MOD_ID, builder ->
            builder.title(Component.translatable(BananaBaking.id(BananaBaking.MOD_ID).toLanguageKey("itemGroup")))
                .icon(() -> com.dontnag.bananabaking.block.BananaBlocks.BAKING_OVEN.createStack())
                .displayItems((displayParameters, output) -> {
                    ITEMS.forEach(output::accept);
                    output.accept(BananaBlocks.BANANA_CAKE);
                    output.accept(BananaBlocks.BAKING_OVEN);
                    output.accept(BananaBlocks.MIXING_BOWL);
                })
        );
    }
}