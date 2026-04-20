package com.dontnag.bananabaking.block.entity;

import com.dontnag.bananabaking.BananaBaking;
import com.dontnag.bananabaking.menus.ContainerMenu;

import net.blay09.mods.balm.api.container.DefaultContainer;
import net.blay09.mods.balm.api.menu.BalmMenuProvider;
import net.blay09.mods.balm.common.BalmBlockEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import org.jetbrains.annotations.Nullable;

public abstract class ContainerBlockEntity<T extends ContainerMenu<?>> extends BalmBlockEntity implements BalmMenuProvider<BlockPos> {

    private boolean changed = false;
    private final DefaultContainer container;

    public ContainerBlockEntity(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState, int size) {
        super(blockEntityType, blockPos, blockState);
        this.container = new DefaultContainer(size) {
            @Override
            public void slotChanged(int slot) {
                changed = true;
                ContainerBlockEntity.this.markDirty();
            }
        };
    }

    public DefaultContainer getContainer(){
        return this.container;
    }

    @Override
    public void setChanged() {
        super.setChanged();
        this.changed = true;
    }

    protected void markDirty(){
        if(this.changed) {
            this.setChanged();
            this.changed = false;
        }
    }

    @Override
    public BlockPos getScreenOpeningData(ServerPlayer player) {
        return this.worldPosition;
    }

    @Override
    public StreamCodec<RegistryFriendlyByteBuf, BlockPos> getScreenStreamCodec() {
        return BlockPos.STREAM_CODEC.cast();
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable(BananaBaking.id(this.getId()).toLanguageKey("container"));
    }

    @Override
    public @Nullable T createMenu(int containerId, Inventory playerInventory, Player player) {
        return this.of(containerId, playerInventory);
    }

    protected abstract String getId();
    protected abstract T of(int syncId, Inventory playerInventory);
}
