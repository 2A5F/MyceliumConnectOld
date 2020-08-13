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

    // init
    @Override
    protected void func_231160_c_() {
        super.func_231160_c_();
        this.widthTooNarrowIn = this.field_230708_k_/* int width */ < 379;

        // field_238742_p_ := int titleX
        this.field_238742_p_ = 20;

        // func_230480_a_ := <T extends Widget> T addButton(T button)
        this.func_230480_a_(new TexturedButton(
                this.guiLeft + 79, this.guiTop + 49, 22, 21,
                0, 213, 22, - 22, GUI_TEXTURE, container::isCooking, (buttonWidget) -> {
            if(!container.isCooking()) {
                container.setCooking(true);
                MCCPackets.instace.sendToServer(new GniteMsg(playerInventory.player.getUniqueID(), true));
            }
        }) {
            // renderToolTip
            @Override
            public void func_230443_a_(@Nonnull MatrixStack matrices, int mouseX, int mouseY) {
                // func_238652_a_ := void renderToolTip(MatrixStack matrices, ITextProperties text, int x, int y)
                GlassKilnScreen.this.func_238652_a_(matrices, itooltip_gnite, mouseX, mouseY);
            }
        });
        // func_230480_a_ := <T extends Widget> T addButton(T button)
        this.func_230480_a_(new TexturedButton(
                this.guiLeft + 105, this.guiTop + 49, 12, 21,
                23, 213, 22, - 22, GUI_TEXTURE, () -> !container.isCooking(), (buttonWidget) -> {
            if(container.isCooking()) {
                container.setCooking(false);
                MCCPackets.instace.sendToServer(new GniteMsg(playerInventory.player.getUniqueID(), false));
            }
        }) {
            // renderToolTip
            @Override
            public void func_230443_a_(@Nonnull MatrixStack matrices, int mouseX, int mouseY) {
                // func_238652_a_ := void renderToolTip(MatrixStack matrices, ITextProperties text, int x, int y)
                GlassKilnScreen.this.func_238652_a_(matrices, itooltip_pause, mouseX, mouseY);
            }
        });
    }



    // tick
    @Override
    public void func_231023_e_() {
        super.func_231023_e_();
    }

    // render
    @Override
    public void func_230430_a_(@Nonnull MatrixStack matrices, int mouseX, int mouseY, float delta) {
        // func_230446_a_ := void renderBackground(MatrixStack matrices)
        this.func_230446_a_(matrices);

        // super.render()
        super.func_230430_a_(matrices, mouseX, mouseY, delta);

        // func_230459_a_ := void drawMouseoverTooltip(MatrixStack matrices, int x, int y)
        this.func_230459_a_(matrices, mouseX, mouseY);
    }

    // drawBackground
    @Override
    protected void func_230450_a_(@Nonnull MatrixStack matrices, float delta, int mouseX, int mouseY) {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        // field_230706_i_ := Minecraft client
        this.field_230706_i_.getTextureManager().bindTexture(GUI_TEXTURE);
        int i = this.guiLeft;
        int j = this.guiTop;
        // func_238474_b_ := void drawTexture(MatrixStack matrices, int x, int y, int u, int v, int width, int height)
        this.func_238474_b_(matrices, i, j, 0, 0, this.xSize, this.ySize);

        if (this.container.isBurning()) {
            int k = this.container.getBurnLeftScaled();
            // func_238474_b_ := void drawTexture(MatrixStack matrices, int x, int y, int u, int v, int width, int height)
            this.func_238474_b_(matrices, i + 56, j + 36 + 12 - k, 176, 12 - k, 14, k + 1);
        }

        int l = this.container.getCookProgressionScaled();
        // func_238474_b_ := void drawTexture(MatrixStack matrices, int x, int y, int u, int v, int width, int height)
        this.func_238474_b_(matrices, i + 79, j + 34, 176, 14, l + 1, 16);
    }

    // drawForeground
    @Override
    protected void func_230451_b_(MatrixStack matrices, int mouseX, int mouseY) {
        this.field_230712_o_.func_238422_b_(matrices, this.field_230704_d_, (float)this.field_238742_p_, (float)this.field_238743_q_, 4210752);
        this.field_230712_o_.func_238422_b_(matrices, this.playerInventory.getDisplayName(), (float)this.field_238744_r_, (float)this.field_238745_s_, 4210752);

        if (this.container.isCooking()) {
            RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
            RenderSystem.enableBlend();
            // field_230706_i_ := Minecraft client
            this.field_230706_i_.getTextureManager().bindTexture(GUI_TEXTURE);
            int i = this.guiLeft;
            int j = this.guiTop;
            // func_238474_b_ := void drawTexture(MatrixStack matrices, int x, int y, int u, int v, int width, int height)
            this.func_238474_b_(matrices, 18, 15, 37, 200, 56, 56);
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
