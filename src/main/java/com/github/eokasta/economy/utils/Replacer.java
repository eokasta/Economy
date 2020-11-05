package com.github.eokasta.economy.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * Replacer instance to improve code readability when replacing a lot of different strings with the same pattern.
 *
 * @Author Gustavo Arantes (https://arantes.dev/)
 */
public class Replacer {

    private Map<CharSequence, CharSequence> replacers;

    public Replacer() {
        this.replacers = new HashMap<>();
    }

    public Replacer add(CharSequence key, Object value) {
        replacers.put(key, value.toString());
        return this;
    }

    public String replace(String message) {
        for (Map.Entry<CharSequence, CharSequence> entry : replacers.entrySet()) {
            message = message.replace(entry.getKey(), entry.getValue());
        }

        return message;
    }

}