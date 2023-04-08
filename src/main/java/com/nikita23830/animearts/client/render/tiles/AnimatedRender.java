package com.nikita23830.animearts.client.render.tiles;

import com.nikita23830.animearts.common.CommonProxy;
import com.nikita23830.animearts.common.block.AnimatedArts;
import com.nikita23830.animearts.common.block.DefaultArts;
import com.nikita23830.animearts.common.tiles.AnimatedArtsTile;
import com.nikita23830.animearts.common.tiles.DefaultAnimeTile;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import org.apache.commons.lang3.math.NumberUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;

public class AnimatedRender extends TileEntitySpecialRenderer {

    public AnimatedRender() {

    }

    public void renderTileEntityAt(TileEntity tileEntity, double x, double y, double z, float f) {
        Vector3f worldTransform = getWorldPosition(Minecraft.getMinecraft(), f);
        Vector3f playersPosition = this.getEntityPosition(x,y,z,f);
        Vector3f renderPosition = new Vector3f(playersPosition.x - worldTransform.x, playersPosition.y - worldTransform.y, playersPosition.z - worldTransform.z);
        AnimatedArtsTile tile = (AnimatedArtsTile)tileEntity;
        int mt = tile.getWorldObj().getBlockMetadata(tile.xCoord, tile.yCoord, tile.zCoord);
        Block block = tile.getWorldObj().getBlock(tile.xCoord, tile.yCoord, tile.zCoord);
        if (!(block instanceof AnimatedArts))
            return;
        this.draw(tileEntity,(float)x,(float)y,(float)z, Integer.toString(mt + 1), 2.5F, tile.getTexture());
    }

    private void draw(TileEntity tileEntity, float x, float y, float z, String corona, float offset, int texture) {
        if (texture == -1)
            return;
        Minecraft mc = Minecraft.getMinecraft();
        float f = 1.6F;
        float f1 = 0.016666668F * f;
        GL11.glPushMatrix();
        GL11.glTranslatef(x + 0.3F, y + 1.1F, z + 0.3F);
        GL11.glNormal3f(0.0F, 1.0F, 0.0F);
        float g = ((AnimatedArtsTile)tileEntity).getViewY();
        GL11.glRotatef(g, 0.0F, 1.0F, 0.0F);
        float scale = 0.1125F;
        GL11.glScalef(-scale, -scale, scale);
        GL11.glDisable(2896);
        GL11.glDepthMask(false);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        Tessellator tessellator = Tessellator.instance;
        GL11.glDisable(3553);
        GL11.glEnable(3553);
        GL11.glDepthMask(true);
        draw(corona, texture, -64/2, -56, 64.0D, 64.0D, 1.0F);
        GL11.glDisable(3042);
        GL11.glEnable(2896);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glPopMatrix();
    }

    public static void draw(String img, int texture, double x, double y, double w, double h, float alpha) {
        int n = NumberUtils.isNumber(img) ? NumberUtils.toInt(img) : 0;
        n = Math.max(Math.min(n, 15), 0);
        if (texture == -1)
            return;
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, alpha);
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.setColorOpaque(255, 255, 255);
        tessellator.setColorRGBA(255, 255, 255, 255);
        tessellator.addVertexWithUV(x, y + h, 0.0D, 0.0D, 1.0D);
        tessellator.addVertexWithUV(x + w, y + h, 0.0D, 1.0D, 1.0D);
        tessellator.addVertexWithUV(x + w, y, 0.0D, 1.0D, 0.0D);
        tessellator.addVertexWithUV(x, y, 0.0D, 0.0D, 0.0D);
        tessellator.draw();
    }

    public Vector3f getWorldPosition(Minecraft mc, float tick) {
        return new Vector3f(
                (float) mc.thePlayer.prevPosX
                        + (float) (mc.thePlayer.posX - mc.thePlayer.prevPosX)
                        * tick,
                (float) mc.thePlayer.prevPosY
                        + (float) (mc.thePlayer.posY - (double) ((float) mc.thePlayer.prevPosY))
                        * tick,
                (float) mc.thePlayer.prevPosZ
                        + (float) (mc.thePlayer.posZ - (double) ((float) mc.thePlayer.prevPosZ))
                        * tick);
    }

    public Vector3f getEntityPosition(double x, double y, double z, float tick) {
        return new Vector3f(
                (float)	x * tick,
                (float) y * tick,
                (float) z * tick);
    }
}

