package vault_work_station;

import Item.ModItems;
import block.ModBlocks;
import block.entity.custom.ModBlockEntities;
import block.entity.custom.menus.screen.ModMenuTypes;
import com.mojang.logging.LogUtils;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(Vault_Work_Station.MOD_ID)
public class Vault_Work_Station {  // Fixed class name to follow Java conventions

    public static final String MOD_ID = "vault_work_station";
    private static final Logger LOGGER = LogUtils.getLogger();

    public Vault_Work_Station() {
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();

        // Register all components
        ModItems.register(eventBus);
        ModBlocks.register(eventBus);
        ModBlockEntities.register(eventBus);
        ModMenuTypes.MENUS.register(eventBus);

        eventBus.addListener(this::setup);
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void setup(final FMLCommonSetupEvent event) {
        // Initialization code if needed
    }
}