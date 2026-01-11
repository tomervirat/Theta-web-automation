package com.crypto.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StringActions {

    private static final Logger LOGGER = LoggerFactory.getLogger(StringActions.class);

    public StringActions() {
    }

    public String convertMultipleLineIntoSingleLine(String string) {
        String result;
        LOGGER.info("Converting multiple line into single line");
        result = string.replaceAll("\\n", " ");
        LOGGER.info("Multiple line string {} converted into single line", string);
        LOGGER.info("Single line string is {}", result);
        return result;
    }

    public boolean containsText(String string, String text) {
        if (string.contains(text)) {
            return true;
        } else {
            return false;
        }
    }
}
