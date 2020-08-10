package co.volight.mycelium_connect.blocks.produce.glasskiln;

import co.volight.mycelium_connect.MCC;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.recipebook.*;
import net.minecraft.client.gui.screen.inventory.AbstractFurnaceScreen;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.FurnaceContainer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;

@OnlyIn(Dist.CLIENT)
public class GlassKilnScreen extends ContainerScreen<GlassKilnContainer> implements IRecipeShownListener {
    public static final String name = GlassKiln.name;

    private static final ResourceLocation GUI_TEXTURE = new ResourceLocation(MCC.ID, "textures/gui/container/" + name + ".png");

    public GlassKilnScreen(GlassKilnContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
        super(screenContainer, inv, titleIn);
    }

    private boolean widthTooNarrowIn;

    // init
    @Override
    protected void func_231160_c_() {
        super.func_231160_c_();
        this.widthTooNarrowIn = this.field_230708_k_/* int width */ < 379;

        // field_238742_p_ := int titleX ; field_230712_o_ := FontRenderer textRenderer ; func_238414_a_ := int getWidth(ITextProperties properties)
        this.field_238742_p_ = (this.xSize - this.field_230712_o_.func_238414_a_( this.field_230704_d_/* ITextComponent title */)) / 2;
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

        super.func_230430_a_(matrices, mouseX, mouseY, delta);
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

    @Override
    public void recipesUpdated() {

    }

    @Override
    public RecipeBookGui getRecipeGui() {
        return null;
    }
}
