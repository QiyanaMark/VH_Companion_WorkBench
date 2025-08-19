package vault_work_station.mixin.enumExtension;

import iskallia.vault.init.ModConfigs;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import vault_work_station.VaultWorkStation;

@Mixin(value = ModConfigs.class, remap = false)
public class ModConfigVaultHuntersMixin {

    @Inject(method = "register", at = @At("TAIL"), remap = false)
    private static void onLoadConfigs(CallbackInfo ci) {
        vault_work_station.config.ModConfigs.register();
        VaultWorkStation.LOGGER.info("Loaded config successfully");
    }
}
