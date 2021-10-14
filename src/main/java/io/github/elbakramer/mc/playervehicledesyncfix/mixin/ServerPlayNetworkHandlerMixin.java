package io.github.elbakramer.mc.playervehicledesyncfix.mixin;

import org.apache.logging.log4j.Logger;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.network.packet.s2c.play.EntityPassengersSetS2CPacket;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;

import io.github.elbakramer.mc.playervehicledesyncfix.PlayerVehicleDesyncFixMod;
import io.github.elbakramer.mc.playervehicledesyncfix.util.PlayerVehicleDesyncFixModConfig;
import io.github.elbakramer.mc.playervehicledesyncfix.util.PlayerVehicleDesyncFixModConfigManager;

@Mixin(ServerPlayNetworkHandler.class)
public class ServerPlayNetworkHandlerMixin {

    @Unique
    private int vehicleDesyncTicks = 0;

    @Final
    @Shadow
    public ServerPlayerEntity player;

    @Inject(method = "onPlayerMove", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/world/ServerChunkManager;updatePosition(Lnet/minecraft/server/network/ServerPlayerEntity;)V", shift = At.Shift.AFTER, ordinal = 0))
    private void onPlayerMoveWithHavingVehicle(PlayerMoveC2SPacket packet, CallbackInfo ci) {
        boolean hasVehicleOnServerSide = player.hasVehicle();
        boolean hasVehicleOnClientSide = !packet.isOnGround() && !packet.changesPosition() && packet.changesLook();
        if (hasVehicleOnServerSide && !hasVehicleOnClientSide) {
            Logger LOGGER = PlayerVehicleDesyncFixMod.LOGGER;
            PlayerVehicleDesyncFixModConfig config = PlayerVehicleDesyncFixModConfigManager.getConfig();
            boolean desyncTicksExeedsLimit = ++vehicleDesyncTicks > config.vehicleDesyncTicksLimit;
            if (config.logOnDesyncFoundInServer || (config.logOnDesyncTicksExceedLimit && desyncTicksExeedsLimit)) {
                LOGGER.warn(
                        "[PlayerVehicleDesyncFix] Vehicle desync found between player {} and vehicle {} for {} ticks!",
                        player.getName().getString(), player.getVehicle().getName().getString(), vehicleDesyncTicks);
            }
            if (config.sendEntityPassengersSetS2CPacketOnDesyncFoundInServer) {
                player.networkHandler.sendPacket(new EntityPassengersSetS2CPacket(player.getVehicle()));
            }
        } else {
            vehicleDesyncTicks = 0;
        }
    }

}
