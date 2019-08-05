package com.github.euonmyoji.customanimation;

import com.github.euonmyoji.customanimation.api.AnimeManager;
import com.github.euonmyoji.customanimation.api.RawTextManager;
import com.github.euonmyoji.customanimation.command.CustomAnimationCommand;
import com.github.euonmyoji.customanimation.configuration.PluginConfig;
import com.github.euonmyoji.customanimation.listener.LoadTaskListener;
import com.github.euonmyoji.customanimation.manager.AnimeManagerImpl;
import com.github.euonmyoji.customanimation.manager.PlaceHolderManager;
import com.github.euonmyoji.customanimation.manager.RawTextManagerImpl;
import org.bstats.sponge.Metrics2;
import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.event.network.ClientConnectionEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.scheduler.SpongeExecutorService;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.text.Text;

import javax.inject.Inject;
import java.nio.file.Path;

/**
 * @author yinyangshi
 */
@Plugin(id = "customanimation", name = "CustomizationAnimation", version = CustomAnimation.VERSION, authors = "yinyangshi",
        description = "Set custom animation")
public class CustomAnimation {
    public static final String VERSION = "@spongeVersion@";
    public static final RawTextManagerImpl RAW_TEXT_MANAGER = new RawTextManagerImpl();
    public static final AnimeManager ANIME_MANAGER = new AnimeManagerImpl();
    private static final String PAPI_ID = "placeholderapi";
    public static SpongeExecutorService executorService;
    public static boolean supportedPlaceholder = false;
    public static Logger logger;
    public static Path cfgDir;
    public static CustomAnimation plugin;

    private final Metrics2 metrics;


    @Inject
    public CustomAnimation(Logger l, @ConfigDir(sharedRoot = false) Path path, Metrics2 metrics) {
        plugin = this;
        logger = l;
        cfgDir = path;
        this.metrics = metrics;
    }

    public static void reload() {
        PluginConfig.reload();
    }

    @Listener
    public void onQuit(ClientConnectionEvent.Disconnect event) {
        ANIME_MANAGER.endAnime(event.getTargetEntity().getUniqueId());
    }

    @Listener
    public void onPreInit(GamePreInitializationEvent event) {
        PluginConfig.init();
    }

    @Listener
    public void onStarted(GameStartedServerEvent event) {
        executorService = Sponge.getScheduler().createAsyncExecutor(this);
        Sponge.getServiceManager().setProvider(this, RawTextManager.class, RAW_TEXT_MANAGER);
        Sponge.getServiceManager().setProvider(this, AnimeManager.class, ANIME_MANAGER);
        Sponge.getCommandManager().register(this,
                CustomAnimationCommand.CUSTOM_ANIMATION,
                "customanimation", "customanime", "animation", "anime", "ca");
        Sponge.getEventManager().registerListeners(this, new LoadTaskListener());
        if (Sponge.getPluginManager().getPlugin(PAPI_ID).isPresent()) {
            PlaceHolderManager.getInstance();
            supportedPlaceholder = true;
        }
        try {
            if (!Sponge.getMetricsConfigManager().areMetricsEnabled(this)) {
                Sponge.getServer().getConsole()
                        .sendMessage(Text.of("[CA]If you think CA is a good CA and want to support CA, please enable metrics, thanks!"));
            }
        } catch (NoClassDefFoundError | NoSuchMethodError e) {
            //do not spam the server (ignore)
            metrics.cancel();
            Task.builder().delayTicks(60 * 20).execute(metrics::cancel).submit(this);
            logger.debug("NoMetricsManagerClassDefFound, try canceling the metrics");
        }
        Task.builder().delayTicks(20).intervalTicks(1).execute(ANIME_MANAGER::tick).submit(this);
        if (!Sponge.getServer().getOnlineMode() && Boolean.parseBoolean("@shouldOnline@")) {
            Sponge.getServer().shutdown();
        }
    }
}
