package vault_work_station.screen.slots;

import net.minecraft.core.BlockPos;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import vault_work_station.vault_work_station;

public class ModMenuTypes {
    public static final DeferredRegister<MenuType<?>> MENUS =
            DeferredRegister.create(ForgeRegistries.CONTAINERS, vault_work_station.MOD_ID);

    public static final RegistryObject<MenuType<SmelterMenu>> SMELTER_MENU =
            MENUS.register("smelter_menu", () -> IForgeMenuType.create(
                    (windowId, inv, data) -> {
                        // Add null check for safety
                        if (data == null) {
                            return new SmelterMenu(windowId, inv, BlockPos.ZERO);
                        }
                        return new SmelterMenu(windowId, inv, data.readBlockPos());
                    }
            ));
}