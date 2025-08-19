package net.lawliet.companionWorkStation.Items;

import net.lawliet.companionWorkStation.CompanionWorkStation;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.lawliet.companionWorkStation.init.CompanionTab;


public abstract class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, CompanionWorkStation.MOD_ID);

    public static final RegistryObject<Item> COMPANION_ESSENCE = ITEMS.register(
            "companion_essence",
            () -> new Item(new Item.Properties().tab(CompanionTab.COMPANION_TAB))

    );

    public static final RegistryObject<Item> COMPANION_FRAGMENT = ITEMS.register(
            "companion_fragment",
            () -> new Item(new Item.Properties().tab(CompanionTab.COMPANION_TAB))
    );



    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);

    }
}

