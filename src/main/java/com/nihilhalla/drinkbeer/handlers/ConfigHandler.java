package com.nihilhalla.drinkbeer.handlers;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;

public class ConfigHandler {
    public static final ForgeConfigSpec.Builder CONFIG_BUILDER = new ForgeConfigSpec.Builder();

    public static final BooleanValue ENABLE_ALCOHOL_DAMAGE;

    static {
        CONFIG_BUILDER.push("Gameplay");

        ENABLE_ALCOHOL_DAMAGE = CONFIG_BUILDER
            .comment("Whether or not we want alcohol to be a damage source.")
            .define("enableAlcholDamage", true);

        CONFIG_BUILDER.pop();
    }

    public static final ForgeConfigSpec INIT = CONFIG_BUILDER.build();
}
