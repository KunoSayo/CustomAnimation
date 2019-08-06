package com.github.euonmyoji.customanimation.configuration;

import com.github.euonmyoji.customanimation.CustomAnimation;
import org.spongepowered.api.Sponge;

import java.io.IOException;
import java.nio.file.Files;

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
        for (String lang : new String[]{"lang/en_US.lang", "lang/zh_CN.lang"}) {
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

    public static void reload() {

    }
}
