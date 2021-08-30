package com.butukay.petowner.config;

import com.butukay.petowner.PetOwner;
import com.butukay.petowner.utils.PetOwnerConfigUtil;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.text.TranslatableText;

public class ModMenu implements ModMenuApi {

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> {
            ConfigBuilder builder = ConfigBuilder.create()
                    .setParentScreen(parent)
                    .setTitle(new TranslatableText("title.petowner.config"));

            ConfigCategory settings = builder.getOrCreateCategory(new TranslatableText("category.petowner.settings"));

            ConfigEntryBuilder entryBuilder = builder.entryBuilder();

            settings.addEntry(entryBuilder.startBooleanToggle(new TranslatableText("option.petowner.enabled"), PetOwner.getConfig().isEnabled())
                    .setDefaultValue(true)
                    .setSaveConsumer(newValue -> PetOwner.getConfig().setEnabled(newValue))
                    .build());

            settings.addEntry(entryBuilder.startEnumSelector(new TranslatableText("option.petowner.usage-mode"), PetOwnerConfig.Mode.class, PetOwner.getConfig().getUsageMode())
                    .setDefaultValue(PetOwnerConfig.Mode.TOGGLE)
                    .setSaveConsumer(newValue -> PetOwner.getConfig().setUsageMode(newValue))
                    .build()
            );

            settings.addEntry(entryBuilder.startBooleanToggle(new TranslatableText("option.petowner.always-shown"), PetOwner.getConfig().isAlwaysShown())
                    .setDefaultValue(true)
                    .setSaveConsumer(newValue -> PetOwner.getConfig().setAlwaysShown(newValue))
                    .build());

            settings.addEntry(entryBuilder.startBooleanToggle(new TranslatableText("option.petowner.action-bar"), PetOwner.getConfig().isActionBar())
                    .setDefaultValue(true)
                    .setSaveConsumer(newValue -> PetOwner.getConfig().setActionBar(newValue))
                    .build());

            settings.addEntry(entryBuilder.startBooleanToggle(new TranslatableText("option.petowner.show-toggle-message"), PetOwner.getConfig().isShowToggleMessage())
                    .setDefaultValue(true)
                    .setSaveConsumer(newValue -> PetOwner.getConfig().setShowToggleMessage(newValue))
                    .build());

            settings.addEntry(entryBuilder.startBooleanToggle(new TranslatableText("option.petowner.show-raw-username"), PetOwner.getConfig().isRawUsername())
                    .setDefaultValue(false)
                    .setSaveConsumer(newValue -> PetOwner.getConfig().setRawUsername(newValue))
                    .build());

            settings.addEntry(entryBuilder.startIntSlider(new TranslatableText("option.petowner.visible-distance"), PetOwner.getConfig().getVisibleDistance(), 1, 128)
                    .setDefaultValue(16)
                    .setSaveConsumer(newValue -> PetOwner.getConfig().setVisibleDistance(newValue))
                    .build());

            builder.setSavingRunnable(PetOwnerConfigUtil::saveConfig);

            return builder.build();
        };
    }
}
