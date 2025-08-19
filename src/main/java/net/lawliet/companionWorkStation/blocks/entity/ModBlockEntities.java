package net.lawliet.companionWorkStation.blocks.entity;

import net.lawliet.companionWorkStation.CompanionWorkStation;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.lawliet.companionWorkStation.blocks.ModBlocks;

public class ModBlockEntities {

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, CompanionWorkStation.MOD_ID);

    public static final RegistryObject<BlockEntityType<CompanionRecyclerBlockEntity>> COMPANION_RECYCLER_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("smelter_block_entity",
                    () -> BlockEntityType.Builder.of(CompanionRecyclerBlockEntity::new,
                            ModBlocks.COMPANION_RECYCLER_BLOCK.get()).build(null));

    public static final RegistryObject<BlockEntityType<CompanionWorkBenchBlockEntity>> COMPANION_WORKBENCH_ENTITY =
            BLOCK_ENTITIES.register("companion_workbench_entity",
                    () -> BlockEntityType.Builder.of(CompanionWorkBenchBlockEntity::new,
                            ModBlocks.COMPANION_WORKBENCH.get()).build(null));
}
