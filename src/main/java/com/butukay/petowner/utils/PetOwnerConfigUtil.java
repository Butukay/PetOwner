package com.butukay.petowner.utils;

import com.butukay.petowner.config.PetOwnerConfig;
import net.fabricmc.loader.api.FabricLoader;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;

public class PetOwnerConfigUtil {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final File configFile = new File(FabricLoader.getInstance().getConfigDir().toFile(), "petOwner.json");

    public static PetOwnerConfig config;

    public static void loadConfig() {
        try {
            if (configFile.exists()) {
                Reader reader = new FileReader(configFile);
                config = GSON.fromJson(reader, PetOwnerConfig.class);
            } else {
                config = new PetOwnerConfig();
                saveConfig();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveConfig() {
        try {
            configFile.createNewFile();

            Writer writer = new FileWriter(configFile, false);

            GSON.toJson(config, writer);

            writer.flush();
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static PetOwnerConfig getConfig() {
        return config;
    }
}
