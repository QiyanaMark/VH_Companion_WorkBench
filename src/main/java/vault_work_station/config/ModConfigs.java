package vault_work_station.config;

import iskallia.vault.config.Config;
import vault_work_station.recipe.companionWorkBench.CompanionWorkBenchRecipesConfig;

import java.util.HashSet;
import java.util.Set;

public class ModConfigs {
    public static final Set<String> INVALID_CONFIG = new HashSet<>();
    public static final Set<Config> CONFIGS = new HashSet<>();
    private static boolean initialized = false;
    public static CompanionRecycleConfig COMPANION_RECYCLE;
    public static CompanionWorkBenchRecipesConfig COMPANION_WORKBENCH_RECIPES;

    public static void register() {
        INVALID_CONFIG.clear();
        if (!CONFIGS.isEmpty()) {
            CONFIGS.forEach(Config::onUnload);
            CONFIGS.clear();
        }

        COMPANION_RECYCLE = new CompanionRecycleConfig().readConfig();
        COMPANION_WORKBENCH_RECIPES = new CompanionWorkBenchRecipesConfig().readConfig();
    }


    public static boolean isInitialized() {
        return initialized;
    }

}
