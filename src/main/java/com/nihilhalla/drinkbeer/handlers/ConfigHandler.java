package com.nihilhalla.drinkbeer.handlers;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;

public class ConfigHandler {
    public static final ForgeConfigSpec.Builder CONFIG_BUILDER = new ForgeConfigSpec.Builder();

    public static final BooleanValue ENABLE_ALCOHOL_DAMAGE;
    public static final BooleanValue ENABLE_DRUNK_EFFECT;

    static {
        CONFIG_BUILDER.push("Gameplay");

        ENABLE_ALCOHOL_DAMAGE = CONFIG_BUILDER
            .comment("Whether or not we want alcohol to be a damage source.")
            .define("enableAlcholDamage", true);

        CONFIG_BUILDER.pop();
    }
    static {
        CONFIG_BUILDER.push("Gameplay");
        
        ENABLE_DRUNK_EFFECT = CONFIG_BUILDER
            .comment("Whether or not we want drinking to have a debuff associated.")
            .define("enableDrunkEffect", true);

        CONFIG_BUILDER.pop();
    }

    public static final ForgeConfigSpec INIT = CONFIG_BUILDER.build();
}
