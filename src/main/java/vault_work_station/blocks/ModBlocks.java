package vault_work_station.blocks;

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
import vault_work_station.init.CompanionTab;
import vault_work_station.Items.ModItems;


import java.util.function.Supplier;

import static vault_work_station.vault_work_station.MOD_ID;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, MOD_ID);

    public static final RegistryObject<Block> SMELTER_BLOCK = registerBlock(
            "smelter_block",
            () -> new SmelterBlock(BlockBehaviour.Properties.of(Material.METAL)
                    .strength(3.5f)
                    .noOcclusion()),
            CompanionTab.COMPANION_TAB
    );

    public static final RegistryObject<Block> COMPANION_WORKBENCH = registerBlock(
            "companion_workbench",
            () -> new CompanionWorkbenchBlock(BlockBehaviour.Properties.of(Material.METAL)
                    .strength(3.5f)
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
