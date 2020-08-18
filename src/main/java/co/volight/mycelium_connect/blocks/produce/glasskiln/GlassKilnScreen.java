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
    public static final ResourceLocation RECIPE_BUTTON_TEXTURE = new ResourceLocation("textures/gui/recipe_button.png");

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
                0, 213, 22, - 22, GUI_TEXTURE, container::isCooking, (buttonWidget) -> {
            if(!container.isCooking()) {
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
        super.tick();
    }

    @Override
    public void render(@Nonnull MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);

        super.render(matrices, mouseX, mouseY, delta);

        // func_230459_a_ := void drawMouseoverTooltip(MatrixStack matrices, int x, int y)
        this.func_230459_a_(matrices, mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(@Nonnull MatrixStack matrices, float delta, int mouseX, int mouseY) {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.getTextureManager().bindTexture(GUI_TEXTURE);
        int i = this.guiLeft;
        int j = this.guiTop;
        // blit := void drawTexture(MatrixStack matrices, int x, int y, int u, int v, int width, int height)
        this.blit(matrices, i, j, 0, 0, this.xSize, this.ySize);

        if (this.container.isBurning()) {
            int k = this.container.getBurnLeftScaled();
            // blit := void drawTexture(MatrixStack matrices, int x, int y, int u, int v, int width, int height)
            this.blit(matrices, i + 56, j + 36 + 12 - k, 176, 12 - k, 14, k + 1);
        }

        int l = this.container.getCookProgressionScaled();
        // blit := void drawTexture(MatrixStack matrices, int x, int y, int u, int v, int width, int height)
        this.blit(matrices, i + 79, j + 34, 176, 14, l + 1, 16);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(@Nonnull MatrixStack matrices, int mouseX, int mouseY) {
        this.font.func_243248_b(matrices, this.title, (float)this.titleX, (float)this.titleY, 4210752);
        this.font.func_243248_b(matrices, this.playerInventory.getDisplayName(), (float)this.playerInventoryTitleX, (float)this.playerInventoryTitleY, 4210752);

        if (this.container.isCooking()) {
            RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
            RenderSystem.enableBlend();
            this.minecraft.getTextureManager().bindTexture(GUI_TEXTURE);
            int i = this.guiLeft;
            int j = this.guiTop;
            // blit := void drawTexture(MatrixStack matrices, int x, int y, int u, int v, int width, int height)
            this.blit(matrices, 18, 15, 37, 200, 56, 56);
            RenderSystem.disableBlend();
        }
    }

    @Override
    public void recipesUpdated() {

    }

    @Override
    public RecipeBookGui getRecipeGui() {
        return null;
    }
}
