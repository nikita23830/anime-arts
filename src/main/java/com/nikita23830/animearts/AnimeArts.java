package com.nikita23830.animearts;

import com.nikita23830.animearts.common.CommonProxy;
import com.nikita23830.animearts.common.SaveData;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.network.FMLNetworkEvent;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

@Mod(modid = AnimeArts.MODID, version = AnimeArts.VERSION, name = "StreamArts")
public class AnimeArts {
    public static final String MODID = "animearts";
    public static final String VERSION = "1.0";
    @Mod.Instance
    public static AnimeArts instance;
    @SidedProxy(
            serverSide = "com.nikita23830.animearts.common.CommonProxy",
            clientSide = "com.nikita23830.animearts.client.ClientProxy"
    )
    public static CommonProxy proxy;
    public static AnimeTab tab;
    
    @EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.init(event);
    }
}
