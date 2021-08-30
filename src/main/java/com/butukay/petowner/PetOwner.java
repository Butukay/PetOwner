package com.butukay.petowner;

import com.butukay.petowner.config.PetOwnerConfig;
import com.butukay.petowner.utils.PetOwnerConfigUtil;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.TranslatableText;

public class PetOwner implements ModInitializer {

    public static KeyBinding keyBinding;

    public static boolean toggleStatus = false;

    @Override
    public void onInitialize() {
        PetOwnerConfigUtil.loadConfig();

        keyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding("key.petowner.toggle", InputUtil.UNKNOWN_KEY.getCode(), "key.category.butukay-tweaks"));

        ClientTickEvents.END_CLIENT_TICK.register(minecraftClient -> {
            if (keyBinding.isUnbound()) return;

            if (getConfig().getUsageMode() == PetOwnerConfig.Mode.TOGGLE) {
                while (keyBinding.wasPressed()) {

                    toggleStatus = !toggleStatus;

                    if (minecraftClient.player != null && getConfig().isShowToggleMessage()) {
                        minecraftClient.player.sendMessage(new TranslatableText("message.petowner." + (toggleStatus ? "enabled" : "disabled")), getConfig().isActionBar());
                    }
                }
            }
        });
    }

    public static PetOwnerConfig getConfig() {
        return PetOwnerConfigUtil.getConfig();
    }
}
