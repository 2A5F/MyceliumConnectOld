package co.volight.mycelium_connect.setup;

import co.volight.mycelium_connect.MCCBlocks;
import co.volight.mycelium_connect.blocks.MyceliumDirt;
import co.volight.mycelium_connect.blocks.fungi.Fungi;
import co.volight.mycelium_connect.blocks.produce.glasskiln.GlassKilnContainer;
import co.volight.mycelium_connect.blocks.produce.glasskiln.GlassKilnScreen;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockDisplayReader;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@OnlyIn(Dist.CLIENT)
public class ClientSetup {
    public ClientSetup() {
        Minecraft minecraft = Minecraft.getInstance();

        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onClientSetup);
    }

    @SubscribeEvent
    public void onClientSetup(FMLCommonSetupEvent e) {
        onSetRenderLayer(e);
        onRegisterBlockColors(e);
        onRegisterScreens(e);
    }

    public void onSetRenderLayer(FMLCommonSetupEvent e) {
        RenderTypeLookup.setRenderLayer(MCCBlocks.fungi, RenderType.getCutout());

        RenderTypeLookup.setRenderLayer(MCCBlocks.myceliumDirt, RenderType.getCutout());
    }

    public void onRegisterBlockColors(FMLCommonSetupEvent e) {
        BlockColors blockColors = Minecraft.getInstance().getBlockColors();

        blockColors.register((BlockState state, IBlockDisplayReader reader, BlockPos pos, int i) -> {
            return Fungi.getColor(state.get(Fungi.POWER));
        }, MCCBlocks.fungi);

        blockColors.register((BlockState state, IBlockDisplayReader reader, BlockPos pos, int i) -> {
            return MyceliumDirt.getColor(state.get(MyceliumDirt.POWER));
        }, MCCBlocks.myceliumDirt);
    }

    public void onRegisterScreens(FMLCommonSetupEvent e) {
        ScreenManager.registerFactory(GlassKilnContainer.type, GlassKilnScreen::new);
    }

}
