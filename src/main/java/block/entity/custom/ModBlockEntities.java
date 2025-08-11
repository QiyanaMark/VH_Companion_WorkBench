package block.entity.custom;

import block.ModBlocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import vault_work_station.vault_work_station;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, vault_work_station.MOD_ID);

    public static final RegistryObject<BlockEntityType<CompanionJuicerEntity>> COMPANION_JUICER =
            BLOCK_ENTITIES.register("companion_juicer.json", () ->
             BlockEntityType.Builder.of(CompanionJuicerEntity::new,
                     ModBlocks.COMPANION_JUICER.get()).build(null));

    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }

}
