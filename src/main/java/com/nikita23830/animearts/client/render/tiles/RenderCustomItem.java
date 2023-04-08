package com.nikita23830.animearts.client.render.tiles;

import com.nikita23830.animearts.AnimeArts;
import com.nikita23830.animearts.common.IAnimeArts;
import com.nikita23830.animearts.common.RegisterAnimeArts;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer;
import org.lwjgl.opengl.GL11;

public class RenderCustomItem implements IItemRenderer {
    //private static final IModelCustom model_arms = AdvancedModelLoader.loadModel(new ResourceLocation("dayz", "models/obj/arms.obj"));
    ResourceLocation loc = new ResourceLocation(AnimeArts.MODID + ":textures/10.png");

    public String name;
    public RenderCustomItem(String name) {
        super();
        this.name = name;
    }

    @Override
    public boolean handleRenderType(ItemStack is, ItemRenderType type) {
        return true;
    }


    @Override
    public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack is, ItemRendererHelper helper) {
        return true;
    }


    @Override
    public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
        try {
            GL11.glPushMatrix();
            float f = 1.0F;
            GL11.glScalef(f, f, f);
            IIcon icon = item.getItem().getIcon(item, 0);
            Tessellator tessellator = Tessellator.instance;
            //Minecraft.getMinecraft().getTextureManager().bindTexture(RenderHelper.getExternalTexture2("xitem-"+name , "../"+ DownloadUtils.DIR+"/blocks/"+name+".png"));
            int mt = item.getItemDamage() + 1;
            mt = Math.max(Math.min(mt, 15), 0);
            IAnimeArts arts = (IAnimeArts) Block.getBlockFromItem(item.getItem());
            String idArt = arts.getIDArts(item);
            if (idArt.isEmpty())
                Minecraft.getMinecraft().getTextureManager().bindTexture(loc);
            else if (arts.isAnimated()) {
                RegisterAnimeArts.AnimeArt art = RegisterAnimeArts.instance.arts.getOrDefault(idArt, null);
                if (art == null || !art.thread.finalize) {
                    Minecraft.getMinecraft().getTextureManager().bindTexture(loc);
                } else {
                    if (art.thread.decoder.images.size() > 0 && art.thread.decoder.converted.size() == 0) {
                        art.thread.decoder.converted();
                        Minecraft.getMinecraft().getTextureManager().bindTexture(loc);
                    } else
                        GL11.glBindTexture(GL11.GL_TEXTURE_2D, art.thread.decoder.converted.get(0));
                }
            } else {
                RegisterAnimeArts.AnimeArt art = RegisterAnimeArts.instance.arts.getOrDefault(idArt, null);
                if (art == null || (art.needDownload && !art.thread.finalize))
                    Minecraft.getMinecraft().getTextureManager().bindTexture(loc);
                else
                    GL11.glBindTexture(GL11.GL_TEXTURE_2D, art.texture.getGlTextureId());
            }


            if(type == ItemRenderType.INVENTORY) {
                GL11.glTranslatef(-1.1F, -1.4F, 0);
                float k = 2F;
                GL11.glScalef(k+0.12F, k+0.6F, 1);
                GL11.glRotatef(50, 1, 1, 0);
                GL11.glRotatef(180, 0, 1, 0);
                GL11.glRotatef(0.1F, 0, -1, 0);
                GL11.glTranslatef(-1, 0.1F, 0);
                GL11.glScalef(1, 1.02F, 1);
                GL11.glDisable(GL11.GL_LIGHTING);
            }

            if(type == ItemRenderType.EQUIPPED_FIRST_PERSON) {
                GL11.glRotatef(43, 0, 1, 0);
                GL11.glRotatef(-8, 1, 0, 1);
                GL11.glTranslatef(-1F, 0.5F, 0.8F);
                float k = 1.2F;
                GL11.glScalef(k, k, 1);
            }

            if(type == ItemRenderType.EQUIPPED) {
                GL11.glRotatef(44, 0, 1, 0);
                GL11.glRotatef(-10, 1, 0, 1);
                GL11.glTranslatef(-1F, 0.4F, 1.2F);
                float k = 1.2F;
                GL11.glScalef(k, k, 1);
            }
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glEnable(GL11.GL_TEXTURE_2D);
            OpenGlHelper.glBlendFunc(770, 771, 1, 0);
            GL11.glColor4f(1,1,1,1);
            ItemRenderer.renderItemIn2D(tessellator, 1, 0, 0, 1, 16, 16, 0.0625F);
            GL11.glPopMatrix();
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }
}
