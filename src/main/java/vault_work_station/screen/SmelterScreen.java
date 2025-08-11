package vault_work_station.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import vault_work_station.vault_work_station;

public class SmelterScreen extends AbstractContainerScreen<SmelterMenu> {

    private static final ResourceLocation TEXTURE =
            new ResourceLocation(vault_work_station.MOD_ID, "textures/gui/smelter_gui.png");

    public SmelterScreen(SmelterMenu container, Inventory inventory, Component title) {
        super(container, inventory, title);
        this.imageWidth = 176;
        this.imageHeight = 166;
    }

    @Override
    protected void renderBg(PoseStack poseStack, float partialTicks, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
        RenderSystem.setShaderTexture(0, TEXTURE);
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        this.blit(poseStack, x, y, 0, 0, imageWidth, imageHeight);

        // Draw progress arrow
        if (menu.getBlockEntity().getCookTime() > 0) {
            int progress = getProgressArrowScaled();
            this.blit(poseStack, x + 79, y + 34, 176, 14, progress + 1, 16);
        }
    }

    private int getProgressArrowScaled() {
        int cookTime = menu.getBlockEntity().getCookTime();
        int cookTimeTotal = menu.getBlockEntity().getCookTimeTotal();
        return cookTimeTotal != 0 ? cookTime * 24 / cookTimeTotal : 0;
    }

    @Override
    public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(poseStack);
        super.render(poseStack, mouseX, mouseY, partialTicks);
        this.renderTooltip(poseStack, mouseX, mouseY);
    }
}
