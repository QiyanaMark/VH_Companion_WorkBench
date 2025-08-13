package vault_work_station;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import vault_work_station.screen.SmelterScreen;
import vault_work_station.blocks.ModBlocks;
import vault_work_station.blocks.entity.ModBlockEntities;
import vault_work_station.Item.ModItems;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import vault_work_station.screen.slots.ModMenuTypes;



@Mod(vault_work_station.MOD_ID)
public class vault_work_station {
    public static final String MOD_ID = "vault_work_station";


    public vault_work_station() {
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();

        // Register DeferredRegistries (ONCE each)
        ModItems.ITEMS.register(modBus);
        ModBlocks.BLOCKS.register(modBus);
        ModBlockEntities.BLOCK_ENTITIES.register(modBus);
        ModMenuTypes.MENUS.register(modBus);
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

        // Register event listeners
        modBus.addListener(this::commonSetup);  // For common setup (networking, etc.)
        modBus.addListener(this::onClientSetup); // For client-only setup (GUI, rendering)

        // Register for server/global events
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        // Non-client setup (e.g., network packets)
    }

    // Client-only setup (GUI registration)
    private void onClientSetup(final FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            MenuScreens.register(ModMenuTypes.SMELTER_MENU.get(), SmelterScreen::new);
        });
    }
}