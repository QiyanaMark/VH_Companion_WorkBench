package vault_work_station.menu;

import net.minecraft.core.BlockPos;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import vault_work_station.vault_work_station;

public class ModMenuTypes {
    public static final DeferredRegister<MenuType<?>> MENUS =
            DeferredRegister.create(ForgeRegistries.CONTAINERS, vault_work_station.MOD_ID);

    // Smelter menu (unchanged)
    public static final RegistryObject<MenuType<SmelterMenu>> SMELTER_MENU =
            MENUS.register("smelter_menu", () -> IForgeMenuType.create(
                    (windowId, inv, data) -> {
                        if (data == null) {
                            return new SmelterMenu(windowId, inv, BlockPos.ZERO);
                        }
                        return new SmelterMenu(windowId, inv, data.readBlockPos());
                    }
            ));

    // Fixed companion workbench menu
    public static final RegistryObject<MenuType<CompanionWorkBenchMenu>> COMPANION_WORKBENCH_MENU =
            MENUS.register("companion_workbench_menu", () -> IForgeMenuType.create(
                    (windowId, inv, data) -> new CompanionWorkBenchMenu(windowId, inv, data == null ? BlockPos.ZERO : data.readBlockPos())
            ));


    public static void register(IEventBus eventBus) {
        MENUS.register(eventBus);
    }
}