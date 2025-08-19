package net.lawliet.companionWorkStation.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.lawliet.companionWorkStation.CompanionWorkStation;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.lawliet.companionWorkStation.menu.CompanionRecyclerMenu;

public class CompanionRecyclerScreen extends AbstractContainerScreen<CompanionRecyclerMenu> {
    private static final ResourceLocation TEXTURE =
            new ResourceLocation(CompanionWorkStation.MOD_ID, "textures/gui/companion_recycle_gui.png");

    private static final int EMPTY_ARROW_U = 83;
    private static final int EMPTY_ARROW_V = 37;
    private static final int FILLED_ARROW_U = 178;
    private static final int FILLED_ARROW_V = 13;
    private static final int ARROW_WIDTH = 10;
    private static final int ARROW_HEIGHT = 15;
    private static final int ARROW_X = 83;  // where to draw in GUI
    private static final int ARROW_Y = 37;

    public CompanionRecyclerScreen(CompanionRecyclerMenu menu, Inventory inv, Component title) {
        super(menu, inv, title);
        this.imageWidth = 173;
        this.imageHeight = 163;
        this.inventoryLabelY = this.imageHeight - 94;
    }

    @Override
    protected void renderBg(PoseStack poseStack, float partialTicks, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        // Draw background
        this.blit(poseStack, x, y, 0, 0, imageWidth, imageHeight);

        // Draw empty arrow background (always full size)
        this.blit(poseStack,
                x + ARROW_X, y + ARROW_Y,
                EMPTY_ARROW_U, EMPTY_ARROW_V,
                ARROW_WIDTH, ARROW_HEIGHT);

        // Calculate progress using the scaled method from menu
        int filledHeight = menu.getSmeltProgressScaled(ARROW_HEIGHT);
        filledHeight = Mth.clamp(filledHeight, 0, ARROW_HEIGHT);

        // Draw filled portion on top of empty arrow - covering it from top to bottom
        if (filledHeight > 0) {
            this.blit(poseStack,
                    x + ARROW_X, y + ARROW_Y, // start at top
                    FILLED_ARROW_U, FILLED_ARROW_V, // start at top of filled texture
                    ARROW_WIDTH, filledHeight); // height of filled part
        }
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
        poseStack.scale(0.8f, 0.8f, 0.8f);
        this.font.draw(poseStack,
                this.title,
                10,
                6,
                0x404040);
        poseStack.popPose();

        // Inventory label
        poseStack.pushPose();
        poseStack.scale(0.8f, 0.8f, 0.8f);
        this.font.draw(poseStack,
                this.playerInventoryTitle,
                10,
                (this.imageHeight - 94)/0.8f,
                0x404040);
        poseStack.popPose();
    }
}