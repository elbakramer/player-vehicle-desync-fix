package io.github.elbakramer.mc.playervehicledesyncfix.util;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;

import me.shedaniel.autoconfig.AutoConfig;

public class PlayerVehicleDesyncFixModMenuIntegration implements ModMenuApi {

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> AutoConfig.getConfigScreen(PlayerVehicleDesyncFixModConfig.class, parent).get();
    }

}
