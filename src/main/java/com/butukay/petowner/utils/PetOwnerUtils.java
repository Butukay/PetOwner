package com.butukay.petowner.utils;

import com.butukay.petowner.PetOwner;
import com.butukay.petowner.mixin.FoxEntityMixin;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.mojang.authlib.GameProfile;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.FoxEntity;
import net.minecraft.entity.passive.HorseBaseEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class PetOwnerUtils {
    private static final LoadingCache<UUID, Optional<String>> usernameCache = CacheBuilder.newBuilder()
            .expireAfterWrite(6, TimeUnit.HOURS)
            .build(new CacheLoader<>() {
                @Override
                public Optional<String> load(UUID key) {
                    CompletableFuture.runAsync(() -> {
                        GameProfile playerProfile = new GameProfile(key, null);
                        playerProfile = MinecraftClient.getInstance().getSessionService().fillProfileProperties(playerProfile, false);
                        usernameCache.put(key, Optional.ofNullable(playerProfile.getName()));
                    });

                    return Optional.of("Loading...");
                }
            });

    public static Optional<String> getNameFromId(UUID uuid) {
        return usernameCache.getUnchecked(uuid);
    }

    public static List<UUID> getOwnerIds(Entity entity) {
        if (entity instanceof TameableEntity tameableEntity) {

            if (tameableEntity.isTamed()) {
                return Collections.singletonList(tameableEntity.getOwnerUuid());
            }
        }

        if (entity instanceof HorseBaseEntity horseBaseEntity) {

            if (horseBaseEntity.isTame()) {
                return Collections.singletonList(horseBaseEntity.getOwnerUuid());
            }
        }

        if (entity instanceof FoxEntity foxEntity) {
            return ((FoxEntityMixin) foxEntity).getTrustedIds();
        }

        return new ArrayList<>();
    }

    public static Text generateUsernameText(Entity entity) {

        List<UUID> ownerIds = PetOwnerUtils.getOwnerIds(entity);

        if (ownerIds.isEmpty()) return null;

        String ownersString = "";

        if (ownerIds.size() > 1) {
            StringBuilder stringBuilder = new StringBuilder();

            for (int i = 0; i < ownerIds.size(); i++) {
                UUID ownerId = ownerIds.get(i);

                if (ownerId == null) return null;

                stringBuilder.append(PetOwnerUtils.getNameFromId(ownerId).orElse("§4ERROR"));

                if (i < ownerIds.size() - 1) {
                    stringBuilder.append(", ");
                }

                ownersString = stringBuilder.toString();
            }
        } else {
            UUID ownerId = ownerIds.get(0);

            ownersString = PetOwnerUtils.getNameFromId(ownerId).orElse("§4ERROR");
        }

        if (PetOwner.getConfig().isRawUsername()) {
            return new LiteralText(ownersString);
        } else {
            return new TranslatableText("text.petowner.owner" + (ownerIds.size() > 1 ? "s" : ""), ownersString);
        }
    }
}
