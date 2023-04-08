package com.nikita23830.animearts.common.tiles;

import com.nikita23830.animearts.client.GifDecoder;
import com.nikita23830.animearts.common.CommonProxy;
import com.nikita23830.animearts.common.ITileArt;
import com.nikita23830.animearts.common.block.AnimatedArts;
import com.nikita23830.animearts.common.network.VanillaPacketDispatcher;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

import java.awt.image.BufferedImage;
import java.util.HashMap;

public class AnimatedArtsTile extends TileEntity implements ITileArt {
    public int currentID = 0;
    HashMap<Integer, DynamicTexture> cache = new HashMap<Integer, DynamicTexture>();
    public float viewY = 0F;
    private String idArt = "";

    @SideOnly(Side.CLIENT)
    @Override
    public void updateEntity() {
        super.updateEntity();
        if (getDecoder() == null)
            return;
        if (getDecoder().converted.size() > 0) {
            ++currentID;
            if (getDecoder().converted.size() <= currentID)
                currentID = 0;
        }
    }

    @Override
    public void setData(String id) {
        idArt = id;
        VanillaPacketDispatcher.dispatchTEToNearbyPlayers(this);
    }

    @Override
    public String getIdArt() {
        return idArt;
    }

    public void setViewY(float f) {
        this.viewY = f;
        VanillaPacketDispatcher.dispatchTEToNearbyPlayers(this);
    }

    public float getViewY() {
        return viewY;
    }

    @Override
    public void writeToNBT(NBTTagCompound n) {
        super.writeToNBT(n);
        n.setFloat("viewY", viewY);
        n.setString("idArt", idArt);
    }

    @Override
    public void readFromNBT(NBTTagCompound n) {
        super.readFromNBT(n);
        viewY = n.getFloat("viewY");
        idArt = n.getString("idArt");
    }

    private GifDecoder getDecoder() {
        try {
            return CommonProxy.register.arts.get(idArt).thread.decoder;
        } catch (NullPointerException e) {
            return null;
        }
    }

    public int getTexture() {
        GifDecoder decoder = getDecoder();
        if (decoder == null)
            return -1;
        if (getDecoder().images.size() > 0 && getDecoder().converted.size() == 0) {
            getDecoder().converted();
        }
        if (getDecoder().converted.size() > 0 && getDecoder().converted.size() > currentID) {
           return getDecoder().converted.get(currentID);
        }
        return -1;
    }

    @Override
    public Packet getDescriptionPacket() {
        NBTTagCompound data = new NBTTagCompound();
        this.writeToNBT(data);
        return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, 2, data);
    }

    @Override
    public void onDataPacket(NetworkManager netManager, S35PacketUpdateTileEntity packet) {
        readFromNBT(packet.func_148857_g());
    }
}
