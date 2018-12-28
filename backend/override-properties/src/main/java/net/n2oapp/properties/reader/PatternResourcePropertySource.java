package net.n2oapp.properties.reader;

import org.springframework.core.env.PropertiesPropertySource;

/**
 * Search resources by pattern
 */
public class PatternResourcePropertySource extends PropertiesPropertySource {

    protected PatternResourcePropertySource(String name, String locationPattern) {
        super(name, PropertiesReader.getPropertiesFromURI(locationPattern));
    }

}
