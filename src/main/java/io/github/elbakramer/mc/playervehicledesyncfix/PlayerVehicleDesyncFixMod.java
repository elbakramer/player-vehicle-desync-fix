package io.github.elbakramer.mc.playervehicledesyncfix;

import net.fabricmc.api.ModInitializer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;

import io.github.elbakramer.mc.playervehicledesyncfix.util.PlayerVehicleDesyncFixModConfig;

public class PlayerVehicleDesyncFixMod implements ModInitializer {
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LogManager.getLogger("PlayerVehicleDesyncFix");

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.
		AutoConfig.register(PlayerVehicleDesyncFixModConfig.class, JanksonConfigSerializer::new);
		LOGGER.info("[PlayerVehicleDesyncFix] Mod Initialized.");
	}
}