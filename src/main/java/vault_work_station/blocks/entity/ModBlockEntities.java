package vault_work_station.blocks.entity;

import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import vault_work_station.vault_work_station;
import vault_work_station.blocks.ModBlocks;
import vault_work_station.blocks.entity.custom.SmelterBlockEntity;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, vault_work_station.MOD_ID);

    public static final RegistryObject<BlockEntityType<SmelterBlockEntity>> SMELTER_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("smelter_block_entity",
                    () -> BlockEntityType.Builder.of(SmelterBlockEntity::new, ModBlocks.SMELTER_BLOCK.get()).build(null));
}