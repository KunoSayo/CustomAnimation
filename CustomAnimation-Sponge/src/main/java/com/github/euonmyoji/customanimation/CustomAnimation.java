package com.github.euonmyoji.customanimation;

import com.github.euonmyoji.customanimation.api.RawTextManager;
import com.github.euonmyoji.customanimation.command.CustomAnimationCommand;
import com.github.euonmyoji.customanimation.configuration.PluginConfig;
import com.github.euonmyoji.customanimation.manager.PlaceHolderManager;
import com.github.euonmyoji.customanimation.manager.RawTextManagerImpl;
import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.scheduler.SpongeExecutorService;

import javax.inject.Inject;
import java.nio.file.Path;

/**
 * @author yinyangshi
 */
@Plugin(id = "customanimation", name = "CustomizationAnimation", version = CustomAnimation.VERSION, authors = "yinyangshi",
        description = "Set custom animation")
public class CustomAnimation {
    public static final String VERSION = "@spongeVersion@";
    public static final String PAPI_ID = "placeholderapi";
    public static SpongeExecutorService executorService;
    public static boolean supportedPlaceholder = false;
    public static Logger logger;
    public static Path cfgDir;
    public static CustomAnimation plugin;
    public static RawTextManagerImpl rawTextManager = new RawTextManagerImpl();


    @Inject
    public CustomAnimation(Logger l, @ConfigDir(sharedRoot = false) Path path) {
        plugin = this;
        logger = l;
        cfgDir = path;
    }

    public static void reload() {
        PluginConfig.reload();
    }

    @Listener
    public void onStarted(GameStartedServerEvent event) {
        executorService = Sponge.getScheduler().createAsyncExecutor(this);
        Sponge.getServiceManager().setProvider(this, RawTextManager.class, rawTextManager);
        Sponge.getCommandManager().register(this,
                CustomAnimationCommand.CUSTOM_ANIMATION,
                "customanimation", "customanime", "animation", "anime", "ca");
        if (Sponge.getPluginManager().getPlugin(PAPI_ID).isPresent()) {
            PlaceHolderManager.getInstance();
            supportedPlaceholder = true;
        }


        if (!Sponge.getServer().getOnlineMode() && Boolean.parseBoolean("@shouldOnline@")) {
            Sponge.getServer().shutdown();
        }
    }
}
