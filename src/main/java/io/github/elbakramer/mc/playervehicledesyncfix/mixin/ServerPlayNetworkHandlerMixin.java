package io.github.elbakramer.mc.playervehicledesyncfix.mixin;

import org.apache.logging.log4j.Logger;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.network.packet.s2c.play.EntityPassengersSetS2CPacket;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;

import me.shedaniel.autoconfig.AutoConfig;

import io.github.elbakramer.mc.playervehicledesyncfix.PlayerVehicleDesyncFixMod;
import io.github.elbakramer.mc.playervehicledesyncfix.util.PlayerVehicleDesyncFixModConfig;

@Mixin(ServerPlayNetworkHandler.class)
public class ServerPlayNetworkHandlerMixin {

    private static final Logger MOD_LOGGER = PlayerVehicleDesyncFixMod.LOGGER;
    private PlayerVehicleDesyncFixModConfig config = AutoConfig.getConfigHolder(PlayerVehicleDesyncFixModConfig.class)
            .getConfig();

    @Final
    @Shadow
    public ServerPlayerEntity player;

    @Inject(method = "onPlayerMove", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/world/ServerChunkManager;updatePosition(Lnet/minecraft/server/network/ServerPlayerEntity;)V", shift = At.Shift.AFTER, ordinal = 0))
    private void onPlayerMoveWithHavingVehicle(PlayerMoveC2SPacket packet, CallbackInfo ci) {
        boolean hasVehicleOnServerSide = player.hasVehicle();
        boolean hasVehicleOnClientSide = !packet.isOnGround() && !packet.changesPosition() && packet.changesLook();
        if (hasVehicleOnServerSide && !hasVehicleOnClientSide) {
            if (config.logOnDesyncFoundInServer) {
                MOD_LOGGER.warn("[PlayerVehicleDesyncFix] Vehicle desync found for player {} and vehicle {}!",
                        player.getName().getString(), player.getVehicle().getName().getString());
            }
            if (config.sendEntityPassengersSetS2CPacketOnDesyncFoundInServer) {
                this.player.networkHandler.sendPacket(new EntityPassengersSetS2CPacket(this.player.getVehicle()));
            }
        }
    }

}
