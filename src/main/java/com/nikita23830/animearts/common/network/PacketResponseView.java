package com.nikita23830.animearts.common.network;

import com.nikita23830.animearts.common.tiles.AnimatedArtsTile;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.WorldSettings;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.event.world.BlockEvent;

public class PacketResponseView implements IMessage, IMessageHandler<PacketResponseView, IMessage> {
    private float view;
    private int xc;
    private int yc;
    private int zc;

    public PacketResponseView() {}

    public PacketResponseView(float view, int x, int y, int z) {
        this.view = view;
        this.xc = x;
        this.yc = y;
        this.zc = z;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.view = buf.readFloat();
        this.xc = buf.readInt();
        this.yc = buf.readInt();
        this.zc = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeFloat(this.view);
        buf.writeInt(this.xc);
        buf.writeInt(this.yc);
        buf.writeInt(this.zc);
    }

    @Override
    public IMessage onMessage(PacketResponseView message, MessageContext ctx) {
        try {
            TileEntity te = ctx.getServerHandler().playerEntity.getEntityWorld().getTileEntity(message.xc, message.yc, message.zc);
            if (!(te instanceof AnimatedArtsTile))
                return null;
            BlockEvent.BreakEvent event = ForgeHooks.onBlockBreakEvent(ctx.getServerHandler().playerEntity.getEntityWorld(), WorldSettings.GameType.SURVIVAL, ctx.getServerHandler().playerEntity, message.xc, message.yc, message.zc);
            if (event.isCanceled())
                return null;
            ((AnimatedArtsTile)te).setViewY(message.view);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}

