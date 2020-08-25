package co.volight.mycelium_connect.blocks.produce.glasskiln;

import co.volight.mycelium_connect.MCC;
import co.volight.mycelium_connect.MCCPackets;
import co.volight.mycelium_connect.gui.widget.TexturedButton;
import co.volight.mycelium_connect.msg.GniteMsg;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.recipebook.*;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;

@OnlyIn(Dist.CLIENT)
public class GlassKilnScreen extends ContainerScreen<GlassKilnContainer> implements IRecipeShownListener {
    public static final String name = GlassKiln.name;

    public static final ResourceLocation GUI_TEXTURE = new ResourceLocation(MCC.ID, "textures/gui/container/" + name + ".png");

    public static final TranslationTextComponent itooltip_gnite = new TranslationTextComponent("gui." + MCC.ID + "." + name + ".tooltip.ignite");
    public static final TranslationTextComponent itooltip_pause = new TranslationTextComponent("gui." + MCC.ID + "." + name + ".tooltip.pause");

    public GlassKilnScreen(GlassKilnContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
        super(screenContainer, inv, titleIn);
    }

    private boolean widthTooNarrowIn;
    
    @Override
    protected void init() {
        super.init();
        this.widthTooNarrowIn = this.width < 379;
        
        this.titleX = 20;

        this.addButton(new TexturedButton(
                this.guiLeft + 79, this.guiTop + 49, 22, 21,
                0, 213, 22, - 22, GUI_TEXTURE, () -> container.isCooking() || !container.canMade, (buttonWidget) -> {
            if(!container.isCooking() && container.canMade) {
                container.setCooking(true);
                MCCPackets.instace.sendToServer(new GniteMsg(playerInventory.player.getUniqueID(), true));
            }
        }) {
            @Override
            public void renderToolTip(@Nonnull MatrixStack matrices, int mouseX, int mouseY) {
                GlassKilnScreen.this.renderTooltip(matrices, itooltip_gnite, mouseX, mouseY);
            }
        });
        this.addButton(new TexturedButton(
                this.guiLeft + 105, this.guiTop + 49, 12, 21,
                23, 213, 22, - 22, GUI_TEXTURE, () -> !container.isCooking(), (buttonWidget) -> {
            if(container.isCooking()) {
                container.setCooking(false);
                MCCPackets.instace.sendToServer(new GniteMsg(playerInventory.player.getUniqueID(), false));
            }
        }) {
            @Override
            public void renderToolTip(@Nonnull MatrixStack matrices, int mouseX, int mouseY) {
                GlassKilnScreen.this.renderTooltip(matrices, itooltip_pause, mouseX, mouseY);
            }
        });
    }

    @Override
    public void tick() {
        this.container.tick();
        super.tick();
    }

    @Override
    public void render(@Nonnull MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);

        super.render(matrices, mouseX, mouseY, delta);

        // func_230459_a_ := void drawMouseoverTooltip(MatrixStack matrices, int x, int y)
        this.func_230459_a_(matrices, mouseX, mouseY);

        this.drawItemStack(this.container.previewItem, 136, 52, "");
    }

    private void drawItemStack(ItemStack stack, int x, int y, String altText) {
        RenderSystem.translatef(0.0F, 0.0F, 32.0F);
        this.setBlitOffset(200);
        this.itemRenderer.zLevel = 200.0F;
        net.minecraft.client.gui.FontRenderer font = stack.getItem().getFontRenderer(stack);
        if (font == null) font = this.font;
        this.itemRenderer.renderItemAndEffectIntoGUI(stack, x + this.guiLeft, y + guiTop);
        this.itemRenderer.renderItemOverlayIntoGUI(font, stack, x + this.guiLeft, y + guiTop, altText);
        this.setBlitOffset(0);
        this.itemRenderer.zLevel = 0.0F;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(@Nonnull MatrixStack matrices, float delta, int mouseX, int mouseY) {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.getTextureManager().bindTexture(GUI_TEXTURE);
        int l = this.guiLeft, t = this.guiTop;
        drawTexture(matrices, l, t, 0, 0, this.xSize, this.ySize);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(@Nonnull MatrixStack matrices, int mouseX, int mouseY) {
        this.font.func_243248_b(matrices, this.title, (float)this.titleX, (float)this.titleY, 4210752);
        this.font.func_243248_b(matrices, this.playerInventory.getDisplayName(), (float)this.playerInventoryTitleX, (float)this.playerInventoryTitleY, 4210752);

        RenderSystem.enableBlend();
        this.minecraft.getTextureManager().bindTexture(GUI_TEXTURE);

        if (this.container.isCooking()) {
            RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
            int i = this.guiLeft;
            int j = this.guiTop;
            drawTexture(matrices, 18, 15, 37, 200, 56, 56);
        }

        int l = this.guiLeft, t = this.guiTop;

        if (this.container.isBurning()) {
            int k = this.container.getBurnLeftScaled();
            drawTexture(matrices, 83, 53 + 12 - k, 4, 177 + 12 - k, 14, k + 1);
        }

        int cp = this.container.getCookProgressionScaled();
        drawTexture(matrices, 103, 27, 37, 175, cp, 16);

    }

    public void drawTexture(MatrixStack matrices, int x, int y, int u, int v, int width, int height) {
        // blit := void drawTexture(MatrixStack matrices, int x, int y, int u, int v, int width, int height)
        super.blit(matrices, x, y, u, v, width, height);
    }

    @Override
    public void recipesUpdated() {

    }

    @Override
    public RecipeBookGui getRecipeGui() {
        return null;
    }
}
