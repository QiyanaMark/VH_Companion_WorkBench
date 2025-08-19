package vault_work_station.event;

import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.entity.player.PlayerEvent;
import vault_work_station.VaultWorkStation;
import vault_work_station.config.ModConfigs;

public class PlayerLogin {

    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        VaultWorkStation.LOGGER.info("Synced recipe to player {}", event.getPlayer());
        ModConfigs.COMPANION_WORKBENCH_RECIPES.syncTo(ModConfigs.COMPANION_WORKBENCH_RECIPES, (ServerPlayer) event.getPlayer());
    }

}
