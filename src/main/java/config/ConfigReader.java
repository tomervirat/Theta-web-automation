package config;

import java.io.File;
import java.text.MessageFormat;
import java.util.Properties;

import org.ini4j.Ini;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.crypto.constants.Constants;

public class ConfigReader {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigReader.class);

    private static final Properties PROPS = new Properties();

    private static Ini ini;

    private ConfigReader() {
    }

    public static String getPropertyValues(String tagName, String key) {
        String value = null;
        try {
            ini = new Ini(new File(Constants.CONFIG_FILE_PATH));
            value = ini.get(tagName, key);
            LOGGER.info(MessageFormat.format(
                    "Fetched Config value for tagName [{0}] and key [{1}] is [{2}]",
                    tagName, key, value));

            if (value == null) {
                LOGGER.warn(MessageFormat.format(
                        "No value found for tagName [{0}] and key [{1}] in config file.",
                        tagName, key));
            }

        } catch (NullPointerException ne) {
            LOGGER.error("NullPointerException occurred while reading config file: ", ne);
        } catch (Exception e) {
            LOGGER.error("Exception occurred while reading config file: ", e);

        }
        return value;
    }
}
