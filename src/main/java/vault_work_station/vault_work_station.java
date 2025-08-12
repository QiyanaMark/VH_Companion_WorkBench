package vault_work_station;
import net.minecraft.client.gui.Gui;
import vault_work_station.screen.SmelterScreen;
import vault_work_station.blocks.ModBlocks;
import vault_work_station.blocks.entity.ModBlockEntities;
import vault_work_station.screen.slots.SmelterMenu;
import vault_work_station.Item.ModItems;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import vault_work_station.screen.slots.ModMenuTypes;
import vault_work_station.screen.slots.SmelterMenu;

@Mod(vault_work_station.MOD_ID)
public class vault_work_station {
    public static final String MOD_ID = "vault_work_station";


    public vault_work_station() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        ModBlockEntities.BLOCK_ENTITIES.register(FMLJavaModLoadingContext.get().getModEventBus());
        ModItems.ITEMS.register(bus);
        ModBlocks.BLOCKS.register(bus);
        ModBlockEntities.BLOCK_ENTITIES.register(bus);
        ModMenuTypes.MENUS.register(FMLJavaModLoadingContext.get().getModEventBus());

        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
            bus.addListener(this::clientSetup);
        });

        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientSetup);

        MinecraftForge.EVENT_BUS.register(this);
    }

    private void clientSetup(final FMLClientSetupEvent event) {
        MenuScreens.register(ModMenuTypes.SMELTER_MENU.get(), SmelterScreen::new);
    }

}
