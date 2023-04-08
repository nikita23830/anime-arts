package com.nikita23830.animearts.common;

import com.nikita23830.animearts.client.DownloadThread;
import com.nikita23830.animearts.client.GifDecoder;
import com.nikita23830.animearts.common.block.AnimatedArts;
import com.nikita23830.animearts.common.block.DefaultArts;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.DynamicTexture;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class RegisterAnimeArts {
    public static RegisterAnimeArts instance;
    public HashMap<String, AnimeArt> arts = new HashMap<String, AnimeArt>();

    public RegisterAnimeArts() {
        instance = this;
    }

    public static ArrayList<AnimeArt> getDefault() {
        ArrayList<AnimeArt> list = new ArrayList<AnimeArt>();
        for (AnimeArt art : instance.arts.values()) {
            if (art.type == AnimeType.DEFAULT)
                list.add(art);
        }
        return list;
    }

    public static ArrayList<AnimeArt> getAnimated() {
        ArrayList<AnimeArt> list = new ArrayList<AnimeArt>();
        for (AnimeArt art : instance.arts.values()) {
            if (art.type == AnimeType.ANIMATED)
                list.add(art);
        }
        return list;
    }

    public static ArrayList<AnimeArt> getNoConverts() {
        ArrayList<AnimeArt> list = new ArrayList<AnimeArt>();
        for (AnimeArt art : instance.arts.values()) {
            if (art.needDownload && art.texture == null && art.thread != null && art.thread.finalize) {
                list.add(art);
            } else if (art.image != null && art.texture == null)
                list.add(art);
        }
        return list;
    }

    public static boolean allFinalize() {
        for (AnimeArt art : instance.arts.values()) {
            if (art.texture == null && art.type == AnimeType.DEFAULT) {
                return false;
            } else if (art.type == AnimeType.ANIMATED && !art.thread.finalize)
                return false;
            else if (art.type == AnimeType.ANIMATED && art.thread.decoder.converted.size() == 0)
                return false;
        }
        return true;
    }

    public static class AnimeArt {
        public final String url;
        public final String name;
        public final AnimeType type;
        public int id;
        public DownloadThread thread;
        public boolean converted = false;
        public GifDecoder decoder;
        public DynamicTexture texture;
        public boolean needDownload = false;
        public BufferedImage image;
        public File file = null;

        public AnimeArt(String url, String name, AnimeType type) {
            this.name = name;
            this.url = url;
            this.type = type;
        }

        public void startDownload() {
            thread = new DownloadThread(url, type == AnimeType.ANIMATED);
        }

        public void setId(int id) {
            this.id = id;
        }
    }

    public static enum AnimeType {
        DEFAULT,
        ANIMATED
    }
}
