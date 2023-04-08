package com.nikita23830.animearts.client;

import com.nikita23830.animearts.AnimeArts;
import com.nikita23830.animearts.client.render.tiles.AnimatedRender;
import com.nikita23830.animearts.client.render.tiles.AnimeRenderDefault;
import com.nikita23830.animearts.client.render.tiles.RenderCustomItem;
import com.nikita23830.animearts.common.CommonProxy;
import com.nikita23830.animearts.common.RegisterAnimeArts;
import com.nikita23830.animearts.common.SaveData;
import com.nikita23830.animearts.common.tiles.AnimatedArtsTile;
import com.nikita23830.animearts.common.tiles.DefaultAnimeTile;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.MinecraftForgeClient;
import org.apache.commons.codec.binary.Base64;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ClientProxy extends CommonProxy {
    private static boolean finish = false;
    public static SaveData data;

    public void init(FMLInitializationEvent event) {
        try {
            data = new SaveData();
            data.readData();
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.init(event);
        ClientRegistry.bindTileEntitySpecialRenderer(DefaultAnimeTile.class, new AnimeRenderDefault());
        ClientRegistry.bindTileEntitySpecialRenderer(AnimatedArtsTile.class, new AnimatedRender());
        MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(AnimeArts.proxy.defaults), new RenderCustomItem("default"));
        MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(AnimeArts.proxy.animated), new RenderCustomItem("animated"));
        FMLCommonHandler.instance().bus().register(this);
    }

    private int cdTick = 0;

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void onTick(TickEvent.ClientTickEvent event) {
        if (cdTick > 0) {
            --cdTick;
            return;
        }
        if (finish)
            return;
        if (SaveData.canStartingDownload == 1) {
            for (RegisterAnimeArts.AnimeArt art : CommonProxy.register.arts.values()) {
                if (art.needDownload)
                    art.startDownload();
            }
            SaveData.canStartingDownload = 2;
            cdTick = 1;
            return;
        }
        List<RegisterAnimeArts.AnimeArt> arts = RegisterAnimeArts.getNoConverts();
        if (arts.size() == 0 && RegisterAnimeArts.allFinalize())
            finish = true;
        for (RegisterAnimeArts.AnimeArt art : arts) {
            if (art.needDownload && art.type != RegisterAnimeArts.AnimeType.ANIMATED) {
                art.thread.putTexture(art);
                cdTick = 3;
                return;
            } else if (art.type != RegisterAnimeArts.AnimeType.ANIMATED) {
                art.texture = new DynamicTexture(art.image);
                cdTick = 10;
                return;
            } else if (art.type == RegisterAnimeArts.AnimeType.ANIMATED && art.thread.finalize && art.thread.decoder.converted.size() == 0) {
                art.thread.decoder.converted();
                cdTick = 20;
                return;
            }
        }
    }

}
