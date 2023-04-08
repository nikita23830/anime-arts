package com.nikita23830.animearts;

import com.nikita23830.animearts.common.CommonProxy;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.List;

public class AnimeTab extends CreativeTabs {

    List list;


    public AnimeTab() {
        super("SC AnimeArts");
    }

    public Item getTabIconItem() {
        return this.getIconItemStack().getItem();
    }

    public boolean hasSearchBar() {
        return true;
    }

    public ItemStack getIconItemStack() {
        return new ItemStack(CommonProxy.defaults);
    }

    public void displayAllReleventItems(List list) {
        this.list = list;
        CommonProxy.defaults.getSubBlocks(Item.getItemFromBlock(CommonProxy.defaults), this, this.list);
        CommonProxy.animated.getSubBlocks(Item.getItemFromBlock(CommonProxy.animated), this, this.list);
    }

    public void addItem(Item item) {
        item.getSubItems(item, this, this.list);
    }

    public void addBlock(Block block) {
        ItemStack stack = new ItemStack(block);
        block.getSubBlocks(stack.getItem(), this, this.list);
    }

    public void addStack(ItemStack stack) {
        this.list.add(stack);
    }

}
