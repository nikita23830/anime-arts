package com.nikita23830.animearts.common.block;

import com.nikita23830.animearts.common.IAnimeArts;
import com.nikita23830.animearts.common.ITileArt;
import com.nikita23830.animearts.common.RegisterAnimeArts;
import com.nikita23830.animearts.common.tiles.DefaultAnimeTile;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class DefaultArts extends Block implements ITileEntityProvider, IAnimeArts {


    public DefaultArts() {
        super(Material.rock);
        this.setResistance(10.0F);
        this.setHardness(10.0F);
        this.setBlockBounds(0, 0, 0, 1, 1, 1);
        setBlockName("anime");
    }

    @Override
    public void getSubBlocks(Item p_149666_1_, CreativeTabs p_149666_2_, List list) {
        ItemStack stack = new ItemStack(this, 1, 0);
        for (RegisterAnimeArts.AnimeArt art : RegisterAnimeArts.getDefault()) {
            NBTTagCompound nbt = new NBTTagCompound();
            nbt.setString("idArt", art.name);
            ItemStack put = stack.copy();
            put.setTagCompound(nbt);
            list.add(put);
        }
    }

    public AxisAlignedBB getCollisionBoundingBoxFromPool(World w, int x, int y, int z) {
        return null;
    }

    public TileEntity createNewTileEntity(World world, int i) {
        return new DefaultAnimeTile();
    }

    public int getRenderType() {
        return -1;
    }

    public boolean isOpaqueCube() {
        return false;
    }

    public boolean renderAsNormalBlock() {
        return false;
    }

    @SideOnly(Side.CLIENT)
    public boolean onBlockActivated(World w, int x, int y, int z, EntityPlayer p, int i, float f0, float f1, float f2) {
        return true;
    }

    @Override
    public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z, EntityPlayer player) {
        return getStack(world, x, y, z);
    }

    @Override
    public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z) {
        return getStack(world, x, y, z);
    }

    private ItemStack getStack(World w, int x, int y, int z) {
        ItemStack stack = new ItemStack(this, 1, 0);
        TileEntity te = w.getTileEntity(x, y, z);
        if (te instanceof ITileArt) {
            NBTTagCompound n = new NBTTagCompound();
            n.setString("idArt", ((ITileArt) te).getIdArt());
            stack.setTagCompound(n);
        }
        return stack;
    }

    @Override
    public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune) {
        ArrayList<ItemStack> ret = new ArrayList<ItemStack>();
        //ret.add(getStack(world, x, y, z));
        return ret;
    }

    @Override
    protected void dropBlockAsItem(World p_149642_1_, int p_149642_2_, int p_149642_3_, int p_149642_4_, ItemStack stack) {
        // NO-OP
    }

    @Override
    public String getIDArts(ItemStack stack) {
        return stack.hasTagCompound() && stack.getTagCompound().hasKey("idArt") ? stack.getTagCompound().getString("idArt") : "";
    }

    @Override
    public void breakBlock(World w, int x, int y, int z, Block p_149749_5_, int p_149749_6_) {
        EntityItem ei = new EntityItem(w, (x + 0.5D), (y + 0.5D), (z + 0.5D), getStack(w, x, y, z));
        ei.delayBeforeCanPickup = 0;
        w.spawnEntityInWorld(ei);
        super.breakBlock(w, x, y, z, p_149749_5_, p_149749_6_);
    }

    @Override
    public boolean isAnimated() {
        return false;
    }
}
