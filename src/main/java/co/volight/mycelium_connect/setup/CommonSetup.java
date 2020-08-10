package co.volight.mycelium_connect.setup;

import co.volight.mycelium_connect.MCCBlocks;
import co.volight.mycelium_connect.MCCStats;
import co.volight.mycelium_connect.blocks.MyceliumDirt;
import co.volight.mycelium_connect.blocks.fungi.Fungi;
import co.volight.mycelium_connect.blocks.produce.glasskiln.GlassKiln;
import co.volight.mycelium_connect.blocks.produce.glasskiln.GlassKilnTileEntity;
import co.volight.mycelium_connect.items.GlassDust;
import co.volight.mycelium_connect.items.GlassNugget;
import co.volight.mycelium_connect.items.GlassShards;
import co.volight.mycelium_connect.items.WorkHammer;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.stats.IStatFormatter;
import net.minecraft.stats.StatType;
import net.minecraft.stats.Stats;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
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
        FMLJavaModLoadingContext.get().getModEventBus().addGenericListener(IRecipeSerializer.class, this::onRegisterRecipeSerializers);
    }

    @SubscribeEvent
    public void onCommonSetup(FMLCommonSetupEvent e) {

    }

    @SubscribeEvent
    public void onRegisterBlocks(RegistryEvent.Register<Block> e) {
        e.getRegistry().register(new Fungi());

        e.getRegistry().register(new MyceliumDirt());

        e.getRegistry().register(new GlassKiln());
    }

    @SubscribeEvent
    public void onRegisterItems(RegistryEvent.Register<Item> e) {
        e.getRegistry().register(MCCBlocks.makeBlockItem(MCCBlocks.fungi));

        e.getRegistry().register(new WorkHammer.NormalWorkHammer());
        e.getRegistry().register(new WorkHammer.StoneWorkHammer());
        e.getRegistry().register(new WorkHammer.IronWorkHammer());
        e.getRegistry().register(new WorkHammer.GoldenWorkHammer());
        e.getRegistry().register(new WorkHammer.DiamondWorkHammer());
        e.getRegistry().register(new WorkHammer.NetheriteWorkHammer());

        e.getRegistry().register(new GlassShards());
        e.getRegistry().register(new GlassDust());
        e.getRegistry().register(new GlassNugget());

        e.getRegistry().register(MCCBlocks.makeBlockItem(MCCBlocks.myceliumDirt));

        e.getRegistry().register(MCCBlocks.makeBlockItem(MCCBlocks.glassKiln));
    }

    @SubscribeEvent
    public void onRegisterTileEntices(RegistryEvent.Register<TileEntityType<?>> e) {
        e.getRegistry().register(GlassKilnTileEntity.type);
    }

    @SubscribeEvent
    public void onRegisterRecipeSerializers(RegistryEvent.Register<IRecipeSerializer<?>> e) {
        //e.getRegistry().register(MultiCountSmelting.SERIALIZER);
    }

}
