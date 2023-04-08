package com.nikita23830.animearts.common.tiles;

import com.nikita23830.animearts.common.ITileArt;
import com.nikita23830.animearts.common.network.VanillaPacketDispatcher;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

public class DefaultAnimeTile extends TileEntity implements ITileArt {
    private String id = "";

    @SideOnly(Side.CLIENT)
    public double getMaxRenderDistanceSquared() {
        return 50096.0D;
    }

    @Override
    public void setData(String id) {
        this.id = id;
        VanillaPacketDispatcher.dispatchTEToNearbyPlayers(this);
    }

    @Override
    public String getIdArt() {
        return id;
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        nbt.setString("idArt", id);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        id = nbt.getString("idArt");
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
