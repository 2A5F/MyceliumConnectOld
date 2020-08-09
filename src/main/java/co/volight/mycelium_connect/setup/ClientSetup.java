package co.volight.mycelium_connect.setup;

import co.volight.mycelium_connect.MCCBlocks;
import co.volight.mycelium_connect.blocks.MyceliumDirt;
import co.volight.mycelium_connect.blocks.fungi.Fungi;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockDisplayReader;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import javax.annotation.Nullable;

@OnlyIn(Dist.CLIENT)
public class ClientSetup {
    public ClientSetup() {
        Minecraft minecraft = Minecraft.getInstance();

        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onClientSetup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onSetRenderLayer);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onRegisterBlockColors);
    }

    @SubscribeEvent
    public void onClientSetup(FMLCommonSetupEvent e) {

    }

    @SubscribeEvent
    public void onSetRenderLayer(FMLCommonSetupEvent e) {
        RenderTypeLookup.setRenderLayer(MCCBlocks.fungi, RenderType.getCutout());

        RenderTypeLookup.setRenderLayer(MCCBlocks.myceliumDirt, RenderType.getCutout());
    }

    @SubscribeEvent
    public void onRegisterBlockColors(FMLCommonSetupEvent e) {
        BlockColors blockColors = Minecraft.getInstance().getBlockColors();

        blockColors.register((BlockState state, IBlockDisplayReader reader, BlockPos pos, int i) -> {
            return Fungi.getColor(state.get(Fungi.POWER));
        }, MCCBlocks.fungi);

        blockColors.register((BlockState state, IBlockDisplayReader reader, BlockPos pos, int i) -> {
            return MyceliumDirt.getColor(state.get(MyceliumDirt.POWER));
        }, MCCBlocks.myceliumDirt);
    }
}
