/*
 * Licensed under the EUPL, Version 1.2.
 * You may obtain a copy of the Licence at:
 * https://joinup.ec.europa.eu/collection/eupl/eupl-text-eupl-12
 */

package net.dries007.tfc.client.screen;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.CapabilityItemHandler;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.dries007.tfc.client.screen.button.AnvilPlanButton;
import net.dries007.tfc.client.screen.button.AnvilStepButton;
import net.dries007.tfc.common.blockentities.AnvilBlockEntity;
import net.dries007.tfc.common.capabilities.forge.*;
import net.dries007.tfc.common.container.AnvilContainer;
import net.dries007.tfc.common.recipes.AnvilRecipe;
import net.dries007.tfc.util.Helpers;

public class AnvilScreen extends BlockEntityScreen<AnvilBlockEntity, AnvilContainer>
{
    public static final ResourceLocation BACKGROUND = Helpers.identifier("textures/gui/anvil.png");

    public AnvilScreen(AnvilContainer container, Inventory playerInventory, Component name)
    {
        super(container, playerInventory, name, BACKGROUND);

        inventoryLabelY += 41;
        imageHeight += 41;
    }

    @Override
    protected void init()
    {
        super.init();

        addRenderableWidget(new AnvilPlanButton(blockEntity, getGuiLeft(), getGuiTop()));
        for (ForgeStep step : ForgeStep.values())
        {
            addRenderableWidget(new AnvilStepButton(step, getGuiLeft(), getGuiTop()));
        }
    }

    @Override
    protected void renderBg(PoseStack poseStack, float partialTicks, int mouseX, int mouseY)
    {
        super.renderBg(poseStack, partialTicks, mouseX, mouseY);

        final Level level = blockEntity.getLevel();
        final int guiLeft = getGuiLeft(), guiTop = getGuiTop();

        assert level != null;

        // Draw the progress indicators
        final int progress = blockEntity.getWorkValue();
        blit(poseStack, guiLeft + 13 + progress, guiTop + 100, 176, 0, 5, 5);

        final int target = blockEntity.getWorkTarget();
        blit(poseStack, guiLeft + 13 + target, guiTop + 94, 181, 0, 5, 5);

        // Draw rule icons
        final IForging forging = blockEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
            .map(cap -> cap.getStackInSlot(AnvilBlockEntity.SLOT_INPUT_MAIN))
            .flatMap(item -> item.getCapability(ForgingCapability.CAPABILITY).resolve())
            .orElse(null);
        if (forging != null)
        {
            final ForgeSteps steps = forging.getSteps();
            final AnvilRecipe recipe = forging.getRecipe(level);
            if (recipe != null)
            {
                final ForgeRule[] rules = recipe.getRules();
                for (int i = 0; i < rules.length; i++)
                {
                    final ForgeRule rule = rules[i];
                    if (rule != null)
                    {
                        final int xOffset = i * 19;

                        // The rule icon
                        blit(poseStack, guiLeft + 64 + xOffset, guiTop + 10, rule.iconX(), rule.iconY(), 32, 32, 10, 10, 256, 256);

                        // The overlay
                        if (rule.matches(steps))
                        {
                            RenderSystem.setShaderColor(0f, 0.6f, 0.2f, 1f); // Green
                        }
                        else
                        {
                            RenderSystem.setShaderColor(1f, 0.4f, 0, 1f); // Red
                        }

                        blit(poseStack, guiLeft + 59 + xOffset, guiTop + 7, 198, rule.overlayY(), 20, 22);
                        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
                    }
                }
            }

            // Draw step icons
            for (int i = 0; i < 3; i++)
            {
                final ForgeStep step = steps.getStep(i);
                if (step != null)
                {
                    // Reverses the placement of the steps to line up better with the rules
                    final int xOffset = (2 - i) * 19;
                    blit(poseStack, guiLeft + 64 + xOffset, guiTop + 31, step.buttonX(), step.buttonY(), 32, 32, 10, 10, 256, 256);
                }
            }
        }
    }
}