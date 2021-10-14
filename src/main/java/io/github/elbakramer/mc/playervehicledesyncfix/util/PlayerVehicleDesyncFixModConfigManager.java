package io.github.elbakramer.mc.playervehicledesyncfix.util;

import java.util.function.Supplier;

import net.minecraft.client.gui.screen.Screen;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;

public class PlayerVehicleDesyncFixModConfigManager {

    public static void register() {
        AutoConfig.register(PlayerVehicleDesyncFixModConfig.class, JanksonConfigSerializer::new);
    }

    public static PlayerVehicleDesyncFixModConfig getConfig() {
        return AutoConfig.getConfigHolder(PlayerVehicleDesyncFixModConfig.class).getConfig();
    }

    public static Supplier<Screen> getConfigScreen(Screen parent) {
        return AutoConfig.getConfigScreen(PlayerVehicleDesyncFixModConfig.class, parent);
    }

}
