package net.lawliet.companionWorkStation.event;

import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.lawliet.companionWorkStation.CompanionWorkStation;
import net.lawliet.companionWorkStation.config.ModConfigs;

public class PlayerLogin {

    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        CompanionWorkStation.LOGGER.info("Synced recipe to player {}", event.getPlayer());
        ModConfigs.COMPANION_WORKBENCH_RECIPES.syncTo(ModConfigs.COMPANION_WORKBENCH_RECIPES, (ServerPlayer) event.getPlayer());
    }

}
