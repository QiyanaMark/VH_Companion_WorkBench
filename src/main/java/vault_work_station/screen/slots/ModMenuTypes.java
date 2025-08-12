package vault_work_station.screen.slots;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.common.extensions.IForgeMenuType;
import vault_work_station.vault_work_station;

public class ModMenuTypes {
    public static final DeferredRegister<MenuType<?>> MENUS =
            DeferredRegister.create(ForgeRegistries.CONTAINERS, vault_work_station.MOD_ID);

    public static final RegistryObject<MenuType<SmelterMenu>> SMELTER_MENU =
            MENUS.register("smelter_menu",
                    () -> IForgeMenuType.create((int windowId, Inventory inv, FriendlyByteBuf data) -> {
                        // read BlockPos from buffer (client) and pass to SmelterMenu
                        return new SmelterMenu(windowId, inv, data.readBlockPos());
                    })
            );
}
