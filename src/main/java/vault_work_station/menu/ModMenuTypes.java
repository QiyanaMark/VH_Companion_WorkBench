package vault_work_station.menu;

import net.minecraft.core.BlockPos;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import vault_work_station.VaultWorkStation;

public class ModMenuTypes {
    public static final DeferredRegister<MenuType<?>> MENUS =
            DeferredRegister.create(ForgeRegistries.CONTAINERS, VaultWorkStation.MOD_ID);

    // Smelter menu (unchanged)
    public static final RegistryObject<MenuType<CompanionRecyclerMenu>> COMPANION_RECYCLER_MENU =
            MENUS.register("companion_recycler_menu", () -> IForgeMenuType.create(
                    (windowId, inv, data) -> {
                        if (data == null) {
                            return new CompanionRecyclerMenu(windowId, inv, BlockPos.ZERO);
                        }
                        return new CompanionRecyclerMenu(windowId, inv, data.readBlockPos());
                    }
            ));

    // Fixed companion workbench menu
    public static final RegistryObject<MenuType<CompanionWorkBenchMenu>> COMPANION_WORKBENCH_MENU =
            MENUS.register("companion_workbench_menu", () -> IForgeMenuType.create(
                    (windowId, inv, data) ->{
                        Level level = inv.player.getLevel();
                        BlockPos pos = data.readBlockPos();
                        return new CompanionWorkBenchMenu(windowId, level, pos, inv);

                    }));


    public static void register(IEventBus eventBus) {
        MENUS.register(eventBus);
    }
}