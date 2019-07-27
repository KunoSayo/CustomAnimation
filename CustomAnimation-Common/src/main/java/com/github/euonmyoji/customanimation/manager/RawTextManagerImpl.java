package com.github.euonmyoji.customanimation.manager;

import com.github.euonmyoji.customanimation.api.RawTextManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author yinyangshi
 */
public class RawTextManagerImpl implements RawTextManager {
    private HashMap<Locale, ResourceBundle> map = new HashMap<>();

    @Override
    public void reload(Path path) throws IOException {
        map.clear();
        IOException e = null;
        for (Path p : Files.list(path).collect(Collectors.toList())) {
            Locale locale = Locale.forLanguageTag(p.getFileName().toString().split(".", 2)[0].replace("_", "-"));
            try (BufferedReader reader = Files.newBufferedReader(p)) {
                ResourceBundle res = new PropertyResourceBundle(reader);
                map.put(locale, res);
            } catch (IOException ex) {
                if (e == null) {
                    e = ex;
                } else {
                    e.addSuppressed(ex);
                }
            }
        }
        if(e != null) {
            throw e;
        }
    }

    @Override
    public String get(String key, Locale locale) {
        ResourceBundle res = map.get(locale);
        if (res == null) {
            if ((res = map.get(Locale.getDefault())) == null) {
                res = map.values().iterator().next();
            }
        }
        try {
            return res.getString(key);
        } catch (ClassCastException | MissingResourceException | NullPointerException e) {
            return key;
        }
    }
}
