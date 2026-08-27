package utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class ConfigReader {

    private static final String DEFAULT_BASE_URL = "https://demoqa.com";

    private static final Properties props = load();

    private ConfigReader() {
    }

    private static Properties load() {
        Properties p = new Properties();
        // config.properties is gitignored (secrets); on CI it may be absent and env vars are used instead
        try (InputStream is = ConfigReader.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (is != null) {
                p.load(is);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to read config.properties", e);
        }
        return p;
    }

    /**
     * Returns the value of {@code key} from config.properties,
     * falling back to the {@code envVar} environment variable when the key is missing or blank.
     */
    public static String get(String key, String envVar) {
        String value = props.getProperty(key);
        if (value == null || value.isBlank()) {
            value = System.getenv(envVar);
        }
        return value;
    }

    /**
     * Base URL for the site under test. Override via {@code -Dbase.url=...}
     * (e.g. to point at a staging environment); defaults to the public demoqa.com.
     */
    public static String baseUrl() {
        String value = System.getProperty("base.url");
        return (value == null || value.isBlank()) ? DEFAULT_BASE_URL : value;
    }
}
