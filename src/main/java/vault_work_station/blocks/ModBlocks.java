package vault_work_station.blocks;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import vault_work_station.Item.CompanionTab;
import vault_work_station.vault_work_station;
import vault_work_station.Item.ModItems;
import vault_work_station.blocks.entity.custom.SmelterBlock;

import java.util.function.Supplier;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, vault_work_station.MOD_ID);

    public static final RegistryObject<Block> SMELTER_BLOCK = registerBlock("smelter_block",
            () -> new SmelterBlock(BlockBehaviour.Properties.of(Material.METAL).strength(3.5f)),
            CompanionTab.COMPANION_TAB);

    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block, CreativeModeTab tab) {
        RegistryObject<T> obj = BLOCKS.register(name, block);
        // register block item
        ModItems.ITEMS.register(name, () -> new BlockItem(obj.get(), new net.minecraft.world.item.Item.Properties().tab(tab)));
        return obj;
    }
}
