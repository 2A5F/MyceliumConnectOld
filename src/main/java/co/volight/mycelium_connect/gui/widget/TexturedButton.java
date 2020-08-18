package co.volight.mycelium_connect.gui.widget;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SoundHandler;
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
        this(x, y, width, height, u, v, hoveredVOffset, disabledVOffset, texture, textureWidth, textureHeight, isDisabled, pressAction, StringTextComponent.EMPTY);
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
        this.x = x;
        this.y = y;
    }

    @Override
    public void renderButton(@Nonnull MatrixStack matrices, int mouseX, int mouseY, float delta) {
        Minecraft minecraft = Minecraft.getInstance();
        minecraft.getTextureManager().bindTexture(this.texture);
        int i = this.v;
        if (this.isDisabled.isDisabled()) {
            i += this.disabledVOffset;
        }
        else if (this.isHovered()) {
            i += this.hoveredVOffset;
        }

        RenderSystem.enableDepthTest();
        // blit := static void drawTexture(MatrixStack matrices, int x, int y, float u, float v, int width, int height, int textureWidth, int textureHeight)
        blit(matrices, this.x, this.y, (float)this.u, (float)i, this.width, this.height, this.textureWidth, this.textureHeight);
        
        if (this.isHovered() && !this.isDisabled.isDisabled()) {
            this.renderToolTip(matrices, mouseX, mouseY);
        }
    }

    @Override
    public void onPress() {
        if (!this.isDisabled.isDisabled()) {
            super.onPress();
        }
    }

    @Override
    public void playDownSound(SoundHandler sound) {
        if (!this.isDisabled.isDisabled()) {
            super.playDownSound(sound);
        }
    }

    public interface IsDisabled {
        boolean isDisabled();
    }
}
