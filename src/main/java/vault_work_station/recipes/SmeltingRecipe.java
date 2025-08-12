package vault_work_station.recipes;

import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.Container;

public class SmeltingRecipe implements Recipe<Container> {
    private final Ingredient input;
    private final ItemStack output;
    private final int processingTime;
    private final ResourceLocation id;

    public SmeltingRecipe(ResourceLocation id, Ingredient input, ItemStack output, int processingTime) {
        this.id = id;
        this.input = input;
        this.output = output;
        this.processingTime = processingTime;
    }

    @Override
    public boolean matches(Container container, Level level) {
        return input.test(container.getItem(0));
    }

    @Override
    public ItemStack assemble(Container container) {
        return output.copy();
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getResultItem() {
        return output.copy();
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipeSerializers.SMELTING_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return ModRecipeTypes.SMELTING.get();
    }

    public int getProcessingTime() {
        return processingTime;
    }

    public static class Serializer implements RecipeSerializer<SmeltingRecipe> {
        @Override
        public SmeltingRecipe fromJson(ResourceLocation id, JsonObject json) {
            Ingredient input = Ingredient.fromJson(json.get("input"));
            ItemStack output = ShapedRecipe.itemStackFromJson(json.getAsJsonObject("output"));
            int time = json.get("processingTime").getAsInt();
            return new SmeltingRecipe(id, input, output, time);
        }

        @Override
        public SmeltingRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buffer) {
            Ingredient input = Ingredient.fromNetwork(buffer);
            ItemStack output = buffer.readItem();
            int time = buffer.readVarInt();
            return new SmeltingRecipe(id, input, output, time);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, SmeltingRecipe recipe) {
            recipe.input.toNetwork(buffer);
            buffer.writeItem(recipe.output);
            buffer.writeVarInt(recipe.processingTime);
        }
    }
}