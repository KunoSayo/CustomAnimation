package com.github.euonmyoji.customanimation.manager;

import me.rojo8399.placeholderapi.PlaceholderService;
import org.spongepowered.api.Sponge;

/**
 * @author yinyangshi
 */
public class PlaceHolderManager {
    private static PlaceHolderManager instance;
    public final PlaceholderService service;

    private PlaceHolderManager() {
        service = Sponge.getServiceManager().provideUnchecked(PlaceholderService.class);
    }

    public static PlaceHolderManager getInstance() {
        if (instance == null) {
            instance = new PlaceHolderManager();
        }
        return instance;
    }
}
