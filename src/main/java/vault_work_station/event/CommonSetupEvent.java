package vault_work_station.event;

import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import vault_work_station.VaultWorkStation;
import vault_work_station.config.ModConfigs;

@Mod.EventBusSubscriber(modid = VaultWorkStation.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class CommonSetupEvent {
    @SubscribeEvent
    public static void setupCommon(FMLCommonSetupEvent event) {
        ModConfigs.register();
    }

}
