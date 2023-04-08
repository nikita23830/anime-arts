package com.nikita23830.animearts.common;

import com.nikita23830.animearts.common.block.DefaultArts;
import cpw.mods.fml.common.FMLCommonHandler;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemBlockWithMetadata;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import java.util.List;

public class ItemBlockWithMetadataAndName extends ItemBlockWithMetadata {

    public ItemBlockWithMetadataAndName(Block par2Block) {
        super(par2Block, par2Block);
    }

    @Override
    public String getUnlocalizedName(ItemStack par1ItemStack) {
        Block b = Block.getBlockFromItem(par1ItemStack.getItem());
        IAnimeArts art = ((IAnimeArts) b);
        if (FMLCommonHandler.instance().getSide().isServer())
            return "animeArt";
        return "animeArt." + (b instanceof DefaultArts ? "default." : "animated.") + CommonProxy.register.arts.get(art.getIDArts(par1ItemStack)).name;
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        Block b = Block.getBlockFromItem(stack.getItem());
        IAnimeArts art = ((IAnimeArts) b);
        RegisterAnimeArts.AnimeArt art1 = CommonProxy.register.arts.getOrDefault(art.getIDArts(stack), null);
        if (art1 == null)
            return "Неизвестный арт :(";
        return art1.type == RegisterAnimeArts.AnimeType.DEFAULT ? "Арт #" + art1.id : "Анимированный арт #" + art1.id;
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer p_77624_2_, List l, boolean p_77624_4_) {
        super.addInformation(stack, p_77624_2_, l, p_77624_4_);
        Block b = Block.getBlockFromItem(stack.getItem());
        IAnimeArts art = ((IAnimeArts) b);
        RegisterAnimeArts.AnimeArt art1 = CommonProxy.register.arts.getOrDefault(art.getIDArts(stack), null);
        if (art1 != null) {
            String[] a = art1.name.split("_");
            int stInd = art1.type == RegisterAnimeArts.AnimeType.DEFAULT ? 0 : 1;
            l.add("§9Коллекия: " + (a[stInd + 2].equalsIgnoreCase("a") ? "Аниме" : "Геншин"));
            l.add("§9Год коллекции: " + a[stInd + 1]);
            l.add("§9Период коллекции: " + getNormalPeriod(a[stInd]));
        }
    }

    private String getNormalPeriod(String k) {
        if (k.equalsIgnoreCase("winter"))
            return "Зима";
        else if (k.equalsIgnoreCase("spring"))
            return "Весна";
        else if (k.equalsIgnoreCase("summer"))
            return "Лето";
        else if (k.equalsIgnoreCase("autumn"))
            return "Осень";
        else if (k.equalsIgnoreCase("special"))
            return "§6Особая коллекция";
        else
            return "§eНеизвестная коллекция";
    }

    @Override
    public boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ, int metadata) {
        String id = stack.hasTagCompound() && stack.getTagCompound().hasKey("idArt") ? stack.getTagCompound().getString("idArt") : "";
        if (!super.placeBlockAt(stack, player, world, x, y, z, side, hitX, hitY, hitZ, metadata))
            return false;
        ITileArt te = (ITileArt) world.getTileEntity(x, y, z);
        te.setData(id);
        return true;
    }
}
