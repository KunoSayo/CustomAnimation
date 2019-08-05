package com.github.euonmyoji.customanimation.listener;

import com.github.euonmyoji.customanimation.api.event.LoadTaskDataEvent;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import org.spongepowered.api.event.Listener;

/**
 * @author yinyangshi
 */
public class LoadTaskListener {

    @Listener
    public void loadTask(LoadTaskDataEvent event) {
        CommentedConfigurationNode cfg = event.getNode();
        String s = cfg.getNode("name").getString();
        switch (s){
            case "move":{

            }
            default:{
                break;
            }
        }
    }
}
