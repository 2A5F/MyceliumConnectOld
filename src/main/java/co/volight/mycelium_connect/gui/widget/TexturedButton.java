package co.volight.mycelium_connect.gui.widget;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

import javax.annotation.Nonnull;

public class TexturedButton extends Button {
    private final ResourceLocation texture;
    private final int u;
    private final int v;
    private final int hoveredVOffset;
    private final int disabledVOffset;
    private final int textureWidth;
    private final int textureHeight;
    private final IsDisabled isDisabled;

    public TexturedButton(int x, int y, int width, int height, int u, int v, int hoveredVOffset, int disabledVOffset, ResourceLocation texture, IPressable pressAction) {
        this(x, y, width, height, u, v, hoveredVOffset, disabledVOffset, texture, 256, 256, () -> false, pressAction);
    }

    public TexturedButton(int x, int y, int width, int height, int u, int v, int hoveredVOffset, int disabledVOffset, ResourceLocation texture, IsDisabled isDisabled, IPressable pressAction) {
        this(x, y, width, height, u, v, hoveredVOffset, disabledVOffset, texture, 256, 256, isDisabled, pressAction);
    }

    public TexturedButton(int x, int y, int width, int height, int u, int v, int hoveredVOffset, int disabledVOffset, ResourceLocation texture, int textureWidth, int textureHeight, IsDisabled isDisabled, IPressable pressAction) {
        this(x, y, width, height, u, v, hoveredVOffset, disabledVOffset, texture, textureWidth, textureHeight, isDisabled, pressAction, StringTextComponent.field_240750_d_/* EMPTY */);
    }

    public TexturedButton(int x, int y, int width, int height, int u, int v, int hoveredVOffset, int disabledVOffset, ResourceLocation texture, int textureWidth, int textureHeight, IsDisabled isDisabled, IPressable pressAction, ITextComponent text) {
        super(x, y, width, height, text, pressAction);
        this.u = u;
        this.v = v;
        this.hoveredVOffset = hoveredVOffset;
        this.disabledVOffset = disabledVOffset;
        this.texture = texture;
        this.textureWidth = textureWidth;
        this.textureHeight = textureHeight;
        this.isDisabled = isDisabled;
    }

    public void setPos(int x, int y) {
        this.field_230690_l_ /* x */ = x;
        this.field_230691_m_ /* y */ = y;
    }

    // renderButton
    public void func_230431_b_(@Nonnull MatrixStack matrices, int mouseX, int mouseY, float delta) {
        Minecraft minecraft = Minecraft.getInstance();
        minecraft.getTextureManager().bindTexture(this.texture);
        int i = this.v;
        if (this.isDisabled.isDisabled()) {
            i += this.disabledVOffset;
        }
        // func_230449_g_ := boolean isHovered()
        else if (this.func_230449_g_()) {
            i += this.hoveredVOffset;
        }

        RenderSystem.enableDepthTest();
        // func_238463_a_ := static void drawTexture(MatrixStack matrices, int x, int y, float u, float v, int width, int height, int textureWidth, int textureHeight)
        // field_230690_l_ := x ; field_230691_m_ := y ; field_230688_j_ := width ; field_230689_k_ := height
        func_238463_a_(matrices, this.field_230690_l_, this.field_230691_m_, (float)this.u, (float)i, this.field_230688_j_, this.field_230689_k_, this.textureWidth, this.textureHeight);

        // func_230449_g_ := boolean isHovered()
        if (this.func_230449_g_()) {
            // func_230443_a_ := void renderToolTip(MatrixStack matrices, int x, int y)
            this.func_230443_a_(matrices, mouseX, mouseY);
        }
    }

    public interface IsDisabled {
        boolean isDisabled();
    }
}
