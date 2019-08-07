package com.github.euonmyoji.customanimation.api;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Locale;

/**
 * @author yinyangshi
 */
public interface RawTextManager {
    String[] SUPPORTED_LANGUAGE_FILES = new String[]{"lang/en_US.lang", "lang/zh_CN.lang"};

    /**
     * get the raw text
     * @param key the key of the text
     * @return the raw text with placeholder
     */
    default String get(String key) {
        return get(key, Locale.getDefault());
    }

    /**
     * get the raw text
     * @param key the key of the text
     * @param locale the language of the raw text
     * @return the raw text with placeholder
     */
    String get(String key, Locale locale);

    void reload(Path path) throws IOException;
}
