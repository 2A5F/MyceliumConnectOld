package co.volight.mycelium_connect.setup;

import co.volight.mycelium_connect.MCCBlocks;
import co.volight.mycelium_connect.MCCPackets;
import co.volight.mycelium_connect.blocks.MyceliumDirt;
import co.volight.mycelium_connect.blocks.fungi.Fungi;
import co.volight.mycelium_connect.blocks.jar.GlassJar;
import co.volight.mycelium_connect.blocks.produce.glasskiln.GlassKiln;
import co.volight.mycelium_connect.blocks.produce.glasskiln.GlassKilnContainer;
import co.volight.mycelium_connect.blocks.produce.glasskiln.GlassKilnTileEntity;
import co.volight.mycelium_connect.items.*;
import net.minecraft.block.Block;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public class CommonSetup {

    public CommonSetup() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onCommonSetup);
        FMLJavaModLoadingContext.get().getModEventBus().addGenericListener(Block.class, this::onRegisterBlocks);
        FMLJavaModLoadingContext.get().getModEventBus().addGenericListener(Item.class, this::onRegisterItems);
        FMLJavaModLoadingContext.get().getModEventBus().addGenericListener(TileEntityType.class, this::onRegisterTileEntices);
        FMLJavaModLoadingContext.get().getModEventBus().addGenericListener(ContainerType.class, this::onRegisterContainerTypes);
        FMLJavaModLoadingContext.get().getModEventBus().addGenericListener(IRecipeSerializer.class, this::onRegisterRecipeSerializers);
    }

    @SubscribeEvent
    public void onCommonSetup(FMLCommonSetupEvent e) {
        MCCPackets.Setup();
    }

    @SubscribeEvent
    public void onRegisterBlocks(RegistryEvent.Register<Block> e) {
        e.getRegistry().register(Fungi.setup());

        e.getRegistry().register(MyceliumDirt.setup());

        e.getRegistry().register(GlassKiln.setup());

        e.getRegistry().register(GlassJar.setup());
    }

    @SubscribeEvent
    public void onRegisterItems(RegistryEvent.Register<Item> e) {
        e.getRegistry().register(MCCBlocks.makeBlockItem(MCCBlocks.fungi));

        e.getRegistry().register(WorkHammer.NormalWorkHammer.setup());
        e.getRegistry().register(WorkHammer.StoneWorkHammer.setup());
        e.getRegistry().register(WorkHammer.IronWorkHammer.setup());
        e.getRegistry().register(WorkHammer.GoldenWorkHammer.setup());
        e.getRegistry().register(WorkHammer.DiamondWorkHammer.setup());
        e.getRegistry().register(WorkHammer.NetheriteWorkHammer.setup());

        e.getRegistry().register(GlassShards.setup());
        e.getRegistry().register(GlassDust.setup());
        e.getRegistry().register(GlassNugget.setup());

        e.getRegistry().register(Bark.setup());

        e.getRegistry().register(Cork.setup());
        e.getRegistry().register(FlatCork.setup());

        e.getRegistry().register(MCCBlocks.makeBlockItem(MCCBlocks.myceliumDirt));

        e.getRegistry().register(MCCBlocks.makeBlockItem(MCCBlocks.glassKiln));

        e.getRegistry().register(MCCBlocks.makeBlockItem(MCCBlocks.glassJar));
    }

    @SubscribeEvent
    public void onRegisterTileEntices(RegistryEvent.Register<TileEntityType<?>> e) {
        e.getRegistry().register(GlassKilnTileEntity.type);
    }

    @SubscribeEvent
    public void onRegisterContainerTypes(RegistryEvent.Register<ContainerType<?>> e) {
        e.getRegistry().register(GlassKilnContainer.type);
    }

    @SubscribeEvent
    public void onRegisterRecipeSerializers(RegistryEvent.Register<IRecipeSerializer<?>> e) {
        //e.getRegistry().register(MultiCountSmelting.SERIALIZER);
    }

}
