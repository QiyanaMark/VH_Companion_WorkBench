package vault_work_station.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import vault_work_station.menu.CompanionRecyclerMenu;
import vault_work_station.VaultWorkStation;

public class CompanionRecyclerScreen extends AbstractContainerScreen<CompanionRecyclerMenu> {
    private static final ResourceLocation TEXTURE =
            new ResourceLocation(VaultWorkStation.MOD_ID, "textures/gui/companion_recycle_gui.png");

    // Arrow configuration (pointing down)
    private static final int ARROW_X = 80;
    private static final int ARROW_Y = 36;
    private static final int ARROW_WIDTH = 22;
    private static final int ARROW_HEIGHT = 16;
    private static final int ARROW_U = 176;
    private static final int ARROW_V = 0;

    public CompanionRecyclerScreen(CompanionRecyclerMenu menu, Inventory inv, Component title) {
        super(menu, inv, title);
        this.imageWidth = 176;
        this.imageHeight = 166;
        this.inventoryLabelY = this.imageHeight - 94;
    }

    @Override
    protected void renderBg(PoseStack poseStack, float partialTicks, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);

        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        this.blit(poseStack, x, y, 0, 0, imageWidth, imageHeight);

        int progress = menu.getSmeltProgress();
        this.blit(poseStack,
                x + ARROW_X,
                y + ARROW_Y,
                ARROW_U, ARROW_V,
                ARROW_WIDTH, progress);
    }

    @Override
    public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
        renderBackground(poseStack);
        super.render(poseStack, mouseX, mouseY, partialTicks);
        renderTooltip(poseStack, mouseX, mouseY);
    }



    @Override
    protected void renderLabels(PoseStack poseStack, int mouseX, int mouseY) {
        poseStack.pushPose();
        poseStack.scale(0.8f, 0.8f, 0.8f); // 80% size
        this.font.draw(poseStack,
                this.title,
                10,  // Adjusted X for scaling
                6,   // Adjusted Y for scaling
                0x404040);
        poseStack.popPose();

        // Inventory label
        poseStack.pushPose();
        poseStack.scale(0.8f, 0.8f, 0.8f);
        this.font.draw(poseStack,
                this.playerInventoryTitle,
                10,
                (this.imageHeight - 94)/0.8f, // Compensate for scaling
                0x404040);
        poseStack.popPose();
    }
}