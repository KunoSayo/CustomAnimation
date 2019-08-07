package com.github.euonmyoji.customanimation.listener;

import com.github.euonmyoji.customanimation.api.event.LoadAnimeDataEvent;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.text.Text;

/**
 * @author yinyangshi
 */
public class LoadTaskListener {

    @Listener
    public void loadTask(LoadAnimeDataEvent event) {
        CommentedConfigurationNode cfg = event.getNode();
        String type = cfg.getNode("type").getString();
        if (type != null) {
            switch (type) {
                case "move": {

                }
                default: {
                    event.onFailure(() -> event.getSource().sendMessage(Text.of("Unknown anime type:" + type)));
                    break;
                }
            }
        }
    }
}
