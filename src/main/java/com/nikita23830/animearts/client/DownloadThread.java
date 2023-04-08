package com.nikita23830.animearts.client;

import com.nikita23830.animearts.AnimeArts;
import com.nikita23830.animearts.common.CommonProxy;
import com.nikita23830.animearts.common.RegisterAnimeArts;
import com.nikita23830.animearts.common.SaveData;
import net.minecraft.client.renderer.texture.DynamicTexture;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.net.URL;

public class DownloadThread extends Thread {
    private String url;
    private boolean animated;
    public BufferedImage image;
    public DynamicTexture texture;
    public boolean finalize = false;
    public GifDecoder decoder;

    public DownloadThread(String url, boolean animated) {
        this.url = url;
        this.animated = animated;
        this.start();
    }

    public DynamicTexture getTexture() {
        if (texture == null)
            return texture = new DynamicTexture(this.image);
        return texture;
    }

    public void putTexture(RegisterAnimeArts.AnimeArt art) {
        art.texture = getTexture();
    }

    @Override
    public void run() {
        super.run();
        if (!this.animated) {
            try {
                this.image = SaveData.instance.addImage(this.url);
                finalize = true;
            } catch (Exception var2) {
                this.image = null;
                var2.printStackTrace();
            }
        } else {
            try {
                decoder = new GifDecoder(this.url);
                finalize = true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
