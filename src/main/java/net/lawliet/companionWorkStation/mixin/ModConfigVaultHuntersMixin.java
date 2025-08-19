package net.lawliet.companionWorkStation.mixin;

import iskallia.vault.init.ModConfigs;
import net.lawliet.companionWorkStation.CompanionWorkStation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = ModConfigs.class, remap = false)
public class ModConfigVaultHuntersMixin {

    @Inject(method = "register", at = @At("TAIL"), remap = false)
    private static void onLoadConfigs(CallbackInfo ci) {
        net.lawliet.companionWorkStation.config.ModConfigs.register();
        CompanionWorkStation.LOGGER.info("Loaded config successfully");
    }
}
