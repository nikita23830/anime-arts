package com.nikita23830.animearts.common;

import com.nikita23830.animearts.AnimeArts;
import com.nikita23830.animearts.AnimeTab;
import com.nikita23830.animearts.common.block.AnimatedArts;
import com.nikita23830.animearts.common.block.DefaultArts;
import com.nikita23830.animearts.common.network.NetworkHandler;
import com.nikita23830.animearts.common.tiles.AnimatedArtsTile;
import com.nikita23830.animearts.common.tiles.DefaultAnimeTile;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;

public class CommonProxy {
    public static RegisterAnimeArts register;
    public static Block defaults;
    public static Block animated;

    public void init(FMLInitializationEvent event) {
        register = new RegisterAnimeArts();
        GameRegistry.registerTileEntity(DefaultAnimeTile.class, "animeArt:default");
        GameRegistry.registerTileEntity(AnimatedArtsTile.class, "animeArt:animate");
        NetworkHandler.init();
        register();
        AnimeArts.tab = new AnimeTab();
    }

    public void register() {
        GameRegistry.registerBlock(defaults = new DefaultArts(), ItemBlockWithMetadataAndName.class, "anime");
        GameRegistry.registerBlock(animated = new AnimatedArts(), ItemBlockWithMetadataAndName.class, "animeAnimated");
    }
}
