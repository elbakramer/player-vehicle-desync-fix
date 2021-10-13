package io.github.elbakramer.mc.playervehicledesyncfix.util;

import java.util.function.Supplier;

import net.minecraft.client.gui.screen.Screen;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;

@Config(name = "player-vehicle-desync-fix")
public class PlayerVehicleDesyncFixModConfig implements ConfigData {

    @ConfigEntry.Category("server")
    @ConfigEntry.Gui.Tooltip(count = 2)
    public int vehicleDesyncTicksLimit = 1;

    @ConfigEntry.Category("server")
    @ConfigEntry.Gui.Tooltip
    public boolean sendEntityPassengersSetS2CPacketOnDesyncFoundInServer = true;

    @ConfigEntry.Category("server")
    @ConfigEntry.Gui.Tooltip
    public boolean logOnDesyncTicksExceedLimit = true;

    @ConfigEntry.Category("server")
    @ConfigEntry.Gui.Tooltip
    public boolean logOnDesyncFoundInServer = false;

    @ConfigEntry.Category("client")
    @ConfigEntry.Gui.Tooltip(count = 2)
    public boolean cancelStartRidingIfVehicleToRideIsTooFarAwayInClient = true;

    @ConfigEntry.Category("client")
    @ConfigEntry.Gui.Tooltip
    public double expandAmountForFindingVehicleNearby = 10.0D;

    @ConfigEntry.Category("client")
    @ConfigEntry.Gui.Tooltip
    public boolean logOnVehicleToRideIsTooFarAwayInClient = true;

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
