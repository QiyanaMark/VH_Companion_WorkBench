package net.lawliet.companionWorkStation;

import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.Logger;
import net.lawliet.companionWorkStation.Items.ModItems;
import net.lawliet.companionWorkStation.blocks.ModBlocks;
import net.lawliet.companionWorkStation.blocks.entity.ModBlockEntities;

import net.lawliet.companionWorkStation.event.PlayerLogin;
import net.lawliet.companionWorkStation.menu.ModMenuTypes;
import org.apache.logging.log4j.LogManager;
import net.lawliet.companionWorkStation.screen.CompanionRecyclerScreen;
import net.lawliet.companionWorkStation.screen.CompanionWorkBenchScreen;

@Mod(CompanionWorkStation.MOD_ID)
public class CompanionWorkStation {
    public static final String MOD_ID = "companion_work_station";
    public static final Logger LOGGER = LogManager.getLogger();

    public CompanionWorkStation() {
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();

        // Register DeferredRegistries (ONCE each)
        ModItems.ITEMS.register(modBus);
        ModBlocks.BLOCKS.register(modBus);
        ModBlockEntities.BLOCK_ENTITIES.register(modBus);
        ModMenuTypes.MENUS.register(modBus);
//        ModRecipeTypes.register(modBus);
        // Register event listeners
        modBus.addListener(this::commonSetup);  // For common setup (networking, etc.)
        modBus.addListener(this::onClientSetup); // For client-only setup (GUI, rendering)

        MinecraftForge.EVENT_BUS.addListener(EventPriority.NORMAL, PlayerLogin::onPlayerLoggedIn);
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
            MenuScreens.register(ModMenuTypes.COMPANION_RECYCLER_MENU.get(), CompanionRecyclerScreen::new);
            MenuScreens.register(ModMenuTypes.COMPANION_WORKBENCH_MENU.get(), CompanionWorkBenchScreen::new);
        });
    }
}