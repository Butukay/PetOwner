package com.butukay.petowner.mixin;


import com.butukay.petowner.PetOwner;
import com.butukay.petowner.config.PetOwnerConfig;
import com.butukay.petowner.utils.PetOwnerUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Arm;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Mixin(MobEntity.class)
public abstract class MobEntityMixin extends LivingEntity {

    protected MobEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Shadow
    public abstract Iterable<ItemStack> getArmorItems();

    @Shadow
    public abstract ItemStack getEquippedStack(EquipmentSlot slot);

    @Shadow
    public abstract void equipStack(EquipmentSlot slot, ItemStack stack);

    @Shadow
    public abstract Arm getMainArm();

    @Inject(method = "interact", at = @At("HEAD"))
    public final void onInteract(PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResult> cir) {
        if (PetOwner.getConfig().getUsageMode().equals(PetOwnerConfig.Mode.CLICK)) {

            List<UUID> ownerIds = PetOwnerUtils.getOwnerIds(this);

            if (ownerIds.isEmpty()) return;

            Text messageText = new TranslatableText("");

            if (ownerIds.size() > 1) {
                StringBuilder stringBuilder = new StringBuilder();

                for (int i = 0; i < ownerIds.size(); i++) {
                    UUID ownerId = ownerIds.get(i);

                    if (ownerId == null) return;

                    Optional<String> usernameString = PetOwnerUtils.getNameFromId(ownerId);

                    stringBuilder.append(usernameString.orElse("text.petowner.error"));

                    if (i < ownerIds.size() - 1) {
                        stringBuilder.append(", ");
                    }

                    messageText = new TranslatableText("text.petowner.owners", stringBuilder.toString());
                }
            } else {
                UUID ownerId = ownerIds.get(0);

                Optional<String> usernameString = PetOwnerUtils.getNameFromId(ownerId);

                messageText = new TranslatableText("text.petowner.owner", usernameString.isPresent() ? usernameString.get() : new TranslatableText("text.petowner.error"));
            }

            player.sendMessage(messageText, PetOwner.getConfig().isActionBar());
        }
    }


}
