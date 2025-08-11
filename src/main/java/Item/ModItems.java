package Item;

import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import vault_work_station.vault_work_station;

public abstract class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, vault_work_station.MOD_ID);

    public static final RegistryObject<Item> COMPANION_ESSENCE = ITEMS.register(
            "companionessence",
            () -> new Item(new Item.Properties().tab(CompanionTab.COMPANION_TAB))

    );

    public static final RegistryObject<Item> PARTICLE_FRAGMENT = ITEMS.register(
            "particlefragment",
            () -> new Item(new Item.Properties().tab(CompanionTab.COMPANION_TAB))
    );

    public static final RegistryObject<Item> RELIC_FRAGMENT = ITEMS.register(
            "relicfragment",
            () -> new Item(new Item.Properties().tab(CompanionTab.COMPANION_TAB))
    );


    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);

    }
}

