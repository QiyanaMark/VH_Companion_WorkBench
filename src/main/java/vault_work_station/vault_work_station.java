package vault_work_station;

import Item.ModItems;
import block.ModBlocks;
import block.entity.custom.ModBlockEntities;
import com.mojang.logging.LogUtils;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(vault_work_station.VAULT_WORK_STATION)
public class vault_work_station {

    public static final String MOD_ID = "vault_work_station";
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();


    public static final String VAULT_WORK_STATION = "vault_work_station";

    public vault_work_station() {
        
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ModBlockEntities.register(eventBus);
        ModBlocks.register(eventBus);

        ModItems.register(eventBus);
        eventBus.addListener(this::setup);

        MinecraftForge.EVENT_BUS.register(this);
    }

    private void setup(final FMLCommonSetupEvent event) {
    }
}
