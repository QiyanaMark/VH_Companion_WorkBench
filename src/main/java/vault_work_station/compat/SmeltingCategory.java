package vault_work_station.compat;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import vault_work_station.blocks.ModBlocks;
import vault_work_station.recipes.SmeltingRecipe;

public class SmeltingCategory implements IRecipeCategory<SmeltingRecipe> {
    public static final ResourceLocation UID = new ResourceLocation("vault_work_station", "smelting");
    private final IDrawable background;
    private final IDrawable icon;

    public SmeltingCategory(mezz.jei.api.gui.IGuiHelper helper) {
        background = helper.createBlankDrawable(150, 50);
        icon = helper.createDrawableIngredient(mezz.jei.api.constants.VanillaTypes.ITEM_STACK, new ItemStack(ModBlocks.SMELTER_BLOCK.get()));
    }

    @Override
    public RecipeType<SmeltingRecipe> getRecipeType() {
        return JEIPlugin.JEI_SMELTING;
    }

    @Override
    public Component getTitle() {
        return Component.literal("Smelter");
    }

    @Override
    public IDrawable getBackground() {
        return background;
    }

    @Override
    public IDrawable getIcon() {
        return icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, SmeltingRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 20, 15).addIngredients(recipe.getIngredients().get(0));
        builder.addSlot(RecipeIngredientRole.OUTPUT, 100, 15).addItemStack(recipe.getResultItem());
    }
}