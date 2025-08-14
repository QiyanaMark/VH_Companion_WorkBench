package vault_work_station;

import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import vault_work_station.Items.ModItems;
import vault_work_station.blocks.ModBlocks;
import vault_work_station.blocks.entity.ModBlockEntities;

import vault_work_station.menu.ModMenuTypes;
import vault_work_station.recipe.ModRecipeTypes;
import vault_work_station.screen.CompanionWorkBenchScreen;
import vault_work_station.screen.SmelterScreen;

@Mod(VaultWorkStation.MOD_ID)
public class VaultWorkStation {
    public static final String MOD_ID = "vault_work_station";

    public VaultWorkStation() {
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();

        // Register DeferredRegistries (ONCE each)
        ModItems.ITEMS.register(modBus);
        ModBlocks.BLOCKS.register(modBus);
        ModBlockEntities.BLOCK_ENTITIES.register(modBus);
        ModMenuTypes.MENUS.register(modBus);
        ModRecipeTypes.register(modBus);
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
            // Register both screen types
            MenuScreens.register(ModMenuTypes.SMELTER_MENU.get(), SmelterScreen::new);
            MenuScreens.register(ModMenuTypes.COMPANION_WORKBENCH_MENU.get(), CompanionWorkBenchScreen::new);
        });
    }
}