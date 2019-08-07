package com.github.euonmyoji.customanimation.configuration;

import com.github.euonmyoji.customanimation.CustomAnimation;
import com.github.euonmyoji.customanimation.api.data.IAnimeData;
import com.github.euonmyoji.customanimation.api.event.LoadAnimeDataEvent;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.channel.MessageReceiver;

import java.io.IOException;
import java.nio.file.Files;

import static com.github.euonmyoji.customanimation.CustomAnimation.ANIME_MANAGER;
import static com.github.euonmyoji.customanimation.api.RawTextManager.SUPPORTED_LANGUAGE_FILES;

/**
 * @author yinyangshi
 */
public class PluginConfig {

    public static int parallelGoal = 16;

    public static void init() {

        try {
            Files.createDirectories(CustomAnimation.cfgDir.resolve("lang"));
        } catch (IOException e) {
            CustomAnimation.logger.warn("create lang dir error", e);
        }
        for (String lang : SUPPORTED_LANGUAGE_FILES) {
            Sponge.getAssetManager().getAsset(CustomAnimation.plugin, lang)
                    .ifPresent(asset -> {
                        try {
                            asset.copyToFile(CustomAnimation.cfgDir.resolve(lang), false);
                        } catch (IOException e) {
                            CustomAnimation.logger.warn("copy language file error", e);
                        }
                    });
        }
        try {
            CustomAnimation.RAW_TEXT_MANAGER.reload(CustomAnimation.cfgDir.resolve("lang"));
        } catch (IOException e) {
            CustomAnimation.logger.warn("load lang data error", e);
        }
    }

    public static void reload(MessageReceiver receiver) {
        try {
            CustomAnimation.RAW_TEXT_MANAGER.reload(CustomAnimation.cfgDir.resolve("lang"));
        } catch (IOException e) {
            receiver.sendMessage(Text.of("load language file failed!"));
            CustomAnimation.logger.warn("load lang data error", e);
        }
        try {
            Files.walk(CustomAnimation.cfgDir.resolve("animations")).filter(path -> !Files.isDirectory(path)).forEach(path -> {
                try {
                    CommentedConfigurationNode cfg = HoconConfigurationLoader.builder().setPath(path).build().load();
                    cfg.getNode("anime").getChildrenMap().forEach((o, o2) -> {
                        LoadAnimeDataEvent event = new LoadAnimeDataEvent(receiver, o2);
                        if(Sponge.getEventManager().post(event)) {
                            receiver.sendMessage(Text.of("canceled load anime file:" + path.getFileName() + "'s anime node:" + o.toString()));
                        } else {
                            IAnimeData data = event.getCurrent();
                            if(data == null) {
                                event.failedAndClear();
                                receiver.sendMessage(Text.of("[Failed]load anime file:" + path.getFileName() + "'s anime node:" + o.toString()));
                            } else {
                                if(ANIME_MANAGER.setAnime(data) != null) {
                                    receiver.sendMessage(Text.of("[Replace Old Anime]load anime file:" + path.getFileName() + "'s anime node:" + o.toString()));
                                }
                            }
                        }
                    });
                } catch (IOException e) {
                    receiver.sendMessage(Text.of("load animations file failed! path:\n" + path));
                    CustomAnimation.logger.warn("load animations file failed!", e);
                }
            });
        } catch (IOException e) {
            receiver.sendMessage(Text.of("walk animations files failed!"));
            CustomAnimation.logger.warn("walk animations files failed!", e);
        }
    }
}
