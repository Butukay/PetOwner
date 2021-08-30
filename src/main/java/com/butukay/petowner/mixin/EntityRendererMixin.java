package com.butukay.petowner.mixin;

import com.butukay.petowner.PetOwner;
import com.butukay.petowner.config.PetOwnerConfig;
import com.butukay.petowner.utils.PetOwnerUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.EntityRendererFactory;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.math.Matrix4f;


import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Mixin(EntityRenderer.class)
public abstract class EntityRendererMixin {
    @Final
    @Shadow
    protected EntityRenderDispatcher dispatcher;

    @Inject(method = "render", at = @At("HEAD"))
    private void render(Entity entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) {

        if (MinecraftClient.getInstance().options.hudHidden) return;

        if (entity.hasPassenger(MinecraftClient.getInstance().player)) return;

        if (!PetOwner.getConfig().isEnabled()) return;

        if (dispatcher.targetedEntity != entity && !PetOwner.getConfig().isAlwaysShown()) return;

        if (PetOwner.getConfig().getUsageMode().equals(PetOwnerConfig.Mode.HOLD) && !PetOwner.keyBinding.isPressed())
            return;

        if (PetOwner.getConfig().getUsageMode().equals(PetOwnerConfig.Mode.CLICK)) return;

        if (PetOwner.toggleStatus) return;

        List<UUID> ownerIds = PetOwnerUtils.getOwnerIds(entity);

        if (ownerIds.isEmpty()) return;

        Text text = PetOwnerUtils.generateUsernameText(entity);

        double d = this.dispatcher.getSquaredDistanceToCamera(entity);
        @SuppressWarnings("rawtypes") EntityRenderer entityRenderer = (EntityRenderer) (Object) this;
        if (d <= 4096.0D) {
            float height = entity.getHeight() + 0.5F;
            int y = 10;

            matrices.push();
            matrices.translate(0.0D, height, 0.0D);
            matrices.multiply(this.dispatcher.getRotation());
            matrices.scale(-0.025F, -0.025F, 0.025F);
            Matrix4f matrix4f = matrices.peek().getModel();
            TextRenderer textRenderer = entityRenderer.getTextRenderer();
            float x = (float) (-textRenderer.getWidth(text) / 2);

            float backgroundOpacity = MinecraftClient.getInstance().options.getTextBackgroundOpacity(0.25F);
            int backgroundColor = (int) (backgroundOpacity * 255.0F) << 24;

            textRenderer.draw(text, x, (float) y, 553648127, false, matrix4f, vertexConsumers, true, backgroundColor, light);
            textRenderer.draw(text, x, (float) y, -1, false, matrix4f, vertexConsumers, false, 0, light);

            matrices.pop();
        }
    }
}
