package vault_work_station.blocks;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import vault_work_station.vault_work_station;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, vault_work_station.MOD_ID);

    public static final RegistryObject<Block> SMELTER = BLOCKS.register("smelter",
            () -> new SmelterBlock(BlockBehaviour.Properties.of(Material.METAL).strength(3f).requiresCorrectToolForDrops()));

}
