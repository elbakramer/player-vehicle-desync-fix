package io.github.elbakramer.mc.playervehicledesyncfix.mixin;

import org.apache.logging.log4j.Logger;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Box;

import me.shedaniel.autoconfig.AutoConfig;

import io.github.elbakramer.mc.playervehicledesyncfix.PlayerVehicleDesyncFixMod;
import io.github.elbakramer.mc.playervehicledesyncfix.util.PlayerVehicleDesyncFixModConfig;

@Mixin(ClientPlayNetworkHandler.class)
public class ClientPlayNetworkHandlerMixin {

    private static final Logger MOD_LOGGER = PlayerVehicleDesyncFixMod.LOGGER;
    private PlayerVehicleDesyncFixModConfig config = AutoConfig.getConfigHolder(PlayerVehicleDesyncFixModConfig.class)
            .getConfig();

    @Final
    @Shadow
    private MinecraftClient client;

    private boolean checkIfEntityIsAroundEntity(Entity entityToFind, Entity entityInCenter, double boxExpandAmount) {
        return !entityInCenter
                .getEntityWorld().getEntitiesByClass(Entity.class,
                        new Box(entityInCenter.getBlockPos()).expand(boxExpandAmount), e -> entityToFind.equals(e))
                .isEmpty();
    }

    @Redirect(method = "onEntityPassengersSet", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;startRiding(Lnet/minecraft/entity/Entity;Z)Z"))
    private boolean onEntityPassengersSetStartRiding(Entity passenger, Entity vehicle, boolean forceRiding) {
        if (passenger == this.client.player) {
            boolean canFindVehicleNearby = checkIfEntityIsAroundEntity(vehicle, passenger,
                    config.expandAmountForFindingVehicleNearby);
            if (!canFindVehicleNearby) {
                if (config.logOnVehicleToRideIsTooFarAwayInClient) {
                    MOD_LOGGER.warn("[PlayerVehicleDesyncFix] Vehicle to ride ({}) is too far away from {}!",
                            vehicle.getName().getString(), passenger.getName().getString());
                }
                if (config.cancelStartRidingIfVehicleToRideIsTooFarAwayInClient) {
                    return false;
                }
            }
        }
        return passenger.startRiding(vehicle, forceRiding);
    }

}
