package com.nikita23830.animearts.common.network;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.renderer.entity.RenderManager;

public class PacketRequestView implements IMessage, IMessageHandler<PacketRequestView, IMessage> {
    private int xc;
    private int yc;
    private int zc;

    public PacketRequestView() {}

    public PacketRequestView(int x, int y, int z) {
        this.xc = x;
        this.yc = y;
        this.zc = z;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.xc = buf.readInt();
        this.yc = buf.readInt();
        this.zc = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(this.xc);
        buf.writeInt(this.yc);
        buf.writeInt(this.zc);
    }

    @Override
    public IMessage onMessage(PacketRequestView message, MessageContext ctx) {
        NetworkHandler.sendToServer(new PacketResponseView(-RenderManager.instance.playerViewY, message.xc, message.yc, message.zc));
        return null;
    }
}
