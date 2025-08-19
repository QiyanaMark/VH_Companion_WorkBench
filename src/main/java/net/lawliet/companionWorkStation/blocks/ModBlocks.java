package net.lawliet.companionWorkStation.blocks;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.lawliet.companionWorkStation.init.CompanionTab;
import net.lawliet.companionWorkStation.Items.ModItems;


import java.util.function.Supplier;

import static net.lawliet.companionWorkStation.CompanionWorkStation.MOD_ID;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, MOD_ID);

    public static final RegistryObject<Block> COMPANION_RECYCLER_BLOCK = registerBlock(
            "companion_recycler",
            () -> new CompanionRecycler(BlockBehaviour.Properties.of(Material.METAL)
                    .strength(3.5f, 6.0f)
                    .sound(SoundType.METAL)
                    .requiresCorrectToolForDrops()
                    .noOcclusion()),
            CompanionTab.COMPANION_TAB
    );

    public static final RegistryObject<Block> COMPANION_WORKBENCH = registerBlock(
            "companion_workbench",
            () -> new CompanionWorkbenchBlock(BlockBehaviour.Properties.of(Material.METAL)
                    .strength(3.5f, 6.0f)
                    .requiresCorrectToolForDrops()
                    .sound(SoundType.METAL)
                    .noOcclusion()),
            CompanionTab.COMPANION_TAB
    );

    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block, CreativeModeTab tab) {
        RegistryObject<T> obj = BLOCKS.register(name, block);
        registerBlockItem(name, obj, tab);
        return obj;
    }

    private static <T extends Block> void registerBlockItem(String name, RegistryObject<T> block, CreativeModeTab tab) {
        ModItems.ITEMS.register(name, () -> new BlockItem(
                block.get(),
                new Item.Properties().tab(tab)
        ));
    }
}
