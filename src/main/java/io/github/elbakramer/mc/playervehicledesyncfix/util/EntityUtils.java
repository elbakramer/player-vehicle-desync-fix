package io.github.elbakramer.mc.playervehicledesyncfix.util;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.Box;

public class EntityUtils {

    private EntityUtils() {
    }

    public static boolean checkIfEntityIsAroundEntity(Entity entityToFind, Entity entityInCenter,
            double boxExpandAmount) {
        return !entityInCenter
                .getEntityWorld().getEntitiesByClass(Entity.class,
                        new Box(entityInCenter.getBlockPos()).expand(boxExpandAmount), e -> entityToFind.equals(e))
                .isEmpty();
    }

}
