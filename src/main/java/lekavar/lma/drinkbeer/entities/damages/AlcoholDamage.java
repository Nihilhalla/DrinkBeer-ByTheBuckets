package lekavar.lma.drinkbeer.entities.damages;

import java.util.Random;
import java.util.random.RandomGenerator;

import net.minecraft.network.chat.Component;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import java.lang.Math;

public class AlcoholDamage extends DamageSource {
    public Double dRandom;
    public static final DamageSource ALCOHOL_DAMAGE = (new DamageSource("drinkbeer.alcohol")).bypassArmor().bypassMagic();
    public AlcoholDamage() {

        super("drinkbeer.alcohol");
    }

    @Override
    public Component getLocalizedDeathMessage(LivingEntity entity) {
        String str = "death.attack." + this.getMsgId();
        return Component.translatable(str, entity.getDisplayName());
    }
}
