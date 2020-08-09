package co.volight.mycelium_connect.setup;

import co.volight.mycelium_connect.MCCBlocks;
import co.volight.mycelium_connect.blocks.MyceliumDirt;
import co.volight.mycelium_connect.blocks.fungi.Fungi;
import co.volight.mycelium_connect.items.GlassDust;
import co.volight.mycelium_connect.items.GlassNugget;
import co.volight.mycelium_connect.items.GlassShards;
import co.volight.mycelium_connect.items.WorkHammer;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public class CommonSetup {

    public CommonSetup() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onCommonSetup);
        FMLJavaModLoadingContext.get().getModEventBus().addGenericListener(Block.class, this::onRegisterBlocks);
        FMLJavaModLoadingContext.get().getModEventBus().addGenericListener(Item.class, this::onRegisterItems);
    }

    @SubscribeEvent
    public void onCommonSetup(FMLCommonSetupEvent e) {

    }

    @SubscribeEvent
    public void onRegisterBlocks(RegistryEvent.Register<Block> e) {
        e.getRegistry().register(new Fungi());

        e.getRegistry().register(new MyceliumDirt());
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
    }
}
