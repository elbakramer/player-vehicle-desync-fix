package io.github.elbakramer.mc.playervehicledesyncfix.util;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;

public class PlayerVehicleDesyncFixModMenuIntegration implements ModMenuApi {

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> PlayerVehicleDesyncFixModConfig.getConfigScreen(parent).get();
    }

}
