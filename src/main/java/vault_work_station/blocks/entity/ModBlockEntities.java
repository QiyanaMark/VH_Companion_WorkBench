package vault_work_station.blocks.entity;

import vault_work_station.blocks.custom.SmelterBlockEntity;
import vault_work_station.vault_work_station;
import vault_work_station.blocks.ModBlocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, vault_work_station.MOD_ID);

    public static final RegistryObject<BlockEntityType<SmelterBlockEntity>> SMELTER_ENTITY = BLOCK_ENTITIES.register("smelter",
            () -> BlockEntityType.Builder.of(SmelterBlockEntity::new, ModBlocks.SMELTER.get()).build(null));
}
