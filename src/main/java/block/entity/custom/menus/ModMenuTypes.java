// File: src/main/java/block/entity/custom/menus/ModMenuTypes.java  // MOVED from screen subpackage
package block.entity.custom.menus;

import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModMenuTypes {
    public static final DeferredRegister<MenuType<?>> MENUS =
            DeferredRegister.create(ForgeRegistries.MENU_TYPES, "vault_work_station");

    public static final RegistryObject<MenuType<CompanionJuicerMenu>> COMPANION_JUICER_MENU =
            MENUS.register("companion_juicer_menu",
                    () -> IForgeMenuType.create(CompanionJuicerMenu::new));
}