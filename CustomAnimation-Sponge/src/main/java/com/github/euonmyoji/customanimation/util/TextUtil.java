package com.github.euonmyoji.customanimation.util;

import com.github.euonmyoji.customanimation.CustomAnimation;
import com.github.euonmyoji.customanimation.manager.PlaceHolderManager;
import me.rojo8399.placeholderapi.PlaceholderService;
import me.rojo8399.placeholderapi.impl.utils.TextUtils;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.TextTemplate;

import java.util.Collections;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import static org.spongepowered.api.text.serializer.TextSerializers.FORMATTING_CODE;

/**
 * @author yinyangshi
 */
public class TextUtil {
    private static final Collector<Map.Entry<String, Object>, ?, Map<String, Object>> FORMAT_COLLECTOR;

    static {
        FORMAT_COLLECTOR = Collectors.toMap(Map.Entry::getKey, (Map.Entry<String, Object> o) -> {
            Object v = o.getValue();
            if (v != null) {
                if (v instanceof Number) {
                    long i = ((Number) v).longValue();
                    double d = ((Number) v).doubleValue();
                    if ((double) i != d) {
                        v = String.format("%.2f", ((Number) v).doubleValue());
                    }
                }
            } else {
                v = o.getKey();
            }
            return v;
        });
    }

    public static TextTemplate toTemplate(String s) {
        if (CustomAnimation.supportedPlaceholder) {
            return TextUtils.toTemplate(FORMATTING_CODE.deserialize(s), PlaceholderService.DEFAULT_PATTERN);
        } else {
            String[] v = s.split("@s");
            if (v.length > 1) {
                Object[] objs = new Object[v.length * 2 - 1];
                boolean r = true;
                for (int i = 0; i < objs.length; ++i) {
                    objs[i] = r ? v[i] : "@s";
                    r = !r;
                }
                return TextTemplate.of(TextTemplate.DEFAULT_OPEN_ARG, TextTemplate.DEFAULT_CLOSE_ARG, objs);
            } else {
                return TextTemplate.of(s);
            }
        }
    }

    public static String parse(Player p, TextTemplate textTemplate) {
        if (CustomAnimation.supportedPlaceholder) {
            return FORMATTING_CODE.serialize(textTemplate.apply(PlaceHolderManager.getInstance().service
                    .fillPlaceholders(textTemplate, p, p).entrySet().stream()
                    .collect(FORMAT_COLLECTOR)).build());
        } else {
            return FORMATTING_CODE.serialize(textTemplate.apply(Collections.singletonMap("@s", p.getName())).build());
        }
    }

}
