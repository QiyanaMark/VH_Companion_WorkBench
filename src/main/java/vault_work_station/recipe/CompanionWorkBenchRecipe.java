package vault_work_station.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CompanionWorkBenchRecipe implements Recipe<Container> {
    private final ResourceLocation id;
    private final ItemStack output;
    private final NonNullList<Ingredient> inputs;
    private final List<ItemStack> additionalCosts;

    public CompanionWorkBenchRecipe(ResourceLocation id, ItemStack output,
                                    NonNullList<Ingredient> inputs,
                                    List<ItemStack> additionalCosts) {
        this.id = id;
        this.output = output;
        this.inputs = inputs;
        this.additionalCosts = additionalCosts;
    }

    @Override
    public boolean matches(Container inv, Level level) {
        for (int i = 0; i < inputs.size(); i++) {
            if (!inputs.get(i).test(inv.getItem(i))) {
                return false;
            }
        }
        return true;
    }

    @Override
    public ItemStack assemble(Container inv) {
        ItemStack result = output.copy();
        if (result.getItem() == ForgeRegistries.ITEMS.getValue(new ResourceLocation("the_vault", "companion_relic"))) {
            applyRandomModifiers(result, inv);
        }
        return result;
    }

    private void applyRandomModifiers(ItemStack stack, Container inv) {
        List<String> modifiers = Arrays.asList(
                "the_vault:coin_cascade",
                "the_vault:companion_challenge",
                "the_vault:coin_pile",
                "the_vault:wooden_cascade",
                "the_vault:wooden_bonus",
                "the_vault:ornate_cascade",
                "the_vault:gilded_cascade",
                "the_vault:living_cascade",
                "the_vault:plentiful"
        );

        Collections.shuffle(modifiers, ((Level) inv).random);
        ListTag modifierList = new ListTag();

        for (int i = 0; i < 2 + ((Level) inv).random.nextInt(2); i++) {
            modifierList.add(StringTag.valueOf(modifiers.get(i)));
        }

        stack.getOrCreateTag().put("Modifiers", modifierList);
    }

    public List<ItemStack> getAdditionalCosts() {
        return additionalCosts;
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
        return Serializer.INSTANCE;
    }

    @Override
    public RecipeType<?> getType() {
        return Type.INSTANCE;
    }

    public static class Type implements RecipeType<CompanionWorkBenchRecipe> {
        private Type() {}
        public static final Type INSTANCE = new Type();
        public static final String ID = "companion_workbench";
    }

    public static class Serializer extends ForgeRegistryEntry<RecipeSerializer<?>>
            implements RecipeSerializer<CompanionWorkBenchRecipe> {
        public static final Serializer INSTANCE = new Serializer();

        @Override
        public CompanionWorkBenchRecipe fromJson(ResourceLocation id, JsonObject json) {
            ItemStack output = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "output"));

            JsonArray ingredients = GsonHelper.getAsJsonArray(json, "ingredients");
            NonNullList<Ingredient> inputs = NonNullList.withSize(2, Ingredient.EMPTY);

            for (int i = 0; i < inputs.size(); i++) {
                inputs.set(i, Ingredient.fromJson(ingredients.get(i)));
            }

            List<ItemStack> additionalCosts = new java.util.ArrayList<>();
            if (json.has("additionalCosts")) {
                JsonArray addCosts = GsonHelper.getAsJsonArray(json, "additionalCosts");
                for (int i = 0; i < addCosts.size(); i++) {
                    additionalCosts.add(ShapedRecipe.itemStackFromJson(addCosts.get(i).getAsJsonObject()));
                }
            }

            return new CompanionWorkBenchRecipe(id, output, inputs, additionalCosts);
        }

        @Nullable
        @Override
        public CompanionWorkBenchRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
            NonNullList<Ingredient> inputs = NonNullList.withSize(buf.readInt(), Ingredient.EMPTY);
            inputs.replaceAll(ignored -> Ingredient.fromNetwork(buf));

            ItemStack output = buf.readItem();
            int addCostSize = buf.readInt();
            List<ItemStack> additionalCosts = new java.util.ArrayList<>(addCostSize);

            for (int i = 0; i < addCostSize; i++) {
                additionalCosts.add(buf.readItem());
            }

            return new CompanionWorkBenchRecipe(id, output, inputs, additionalCosts);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, CompanionWorkBenchRecipe recipe) {
            buf.writeInt(recipe.inputs.size());
            recipe.inputs.forEach(ing -> ing.toNetwork(buf));
            buf.writeItem(recipe.output);
            buf.writeInt(recipe.additionalCosts.size());
            recipe.additionalCosts.forEach(buf::writeItem);
        }
    }
}