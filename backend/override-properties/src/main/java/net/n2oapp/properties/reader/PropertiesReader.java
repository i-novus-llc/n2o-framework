package net.n2oapp.properties.reader;


import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.n2oapp.properties.OverrideProperties;
import net.n2oapp.properties.ReloadableProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * User: operhod
 * Date: 11.10.13
 * Time: 12:03
 * PropertiesReader reads and loads properties from files in classpath.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PropertiesReader {

    private static Logger log = LoggerFactory.getLogger(PropertiesReader.class);

    public static OverrideProperties getPropertiesFromClasspath(String name) {
        return getPropertiesFromURI("classpath*:" + name);
    }

    public static OverrideProperties getPropertiesFromURI(String locationPattern) {
        OverrideProperties properties = new OverrideProperties();
        try {
            PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
            for (Resource resource : resolver.getResources(locationPattern)) {
                try (InputStream is = resource.getInputStream()) {
                    if (is != null) {
                        loadPropertiesFromStream(properties, is);
                    } else {
                        log.debug("{} not found.", locationPattern);
                    }
                }
            }
            return properties;
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    private static void loadPropertiesFromStream(OverrideProperties properties, InputStream is) {
        try {
            properties.load(is);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    public static OverrideProperties getPropertiesFromClasspath(OverrideProperties... parentProperties) {
        OverrideProperties prop = null;
        OverrideProperties[] reverseProps = reverse(parentProperties);
        for (OverrideProperties current : reverseProps) {
            if (prop == null)
                prop = current;
            else if (current != null) {
                current.setBaseProperties(prop);
                prop = current;
            }
        }
        return prop;
    }

    public static OverrideProperties getPropertiesFromClasspath(String... paths) {
        OverrideProperties[] props = new OverrideProperties[paths.length];
        for (int i = 0; i < paths.length; i++) {
            props[i] = getPropertiesFromClasspath(paths[i]);
        }
        return getPropertiesFromClasspath(props);
    }

    public static ReloadableProperties getReloadableFromClasspath(String name, int cacheTime) {
        return getReloadableFromResource(getClasspathResource(name), cacheTime);
    }

    public static ReloadableProperties getReloadableFromFilesystem(String location, int cacheTime) {
        return getReloadableFromResource(getFilesystemResource(location), cacheTime);
    }

    public static ReloadableProperties getReloadableFromResource(Resource resource, int cacheTime) {
        ReloadableProperties properties = new ReloadableProperties();
        properties.setResource(resource);
        properties.setCacheTime(cacheTime);
        return properties;
    }

    private static OverrideProperties[] reverse(OverrideProperties[] props) {
        OverrideProperties[] tmp = new OverrideProperties[props.length];
        for (int i = 0; i < props.length; i++) {
            tmp[i] = props[props.length - i - 1];
        }
        return tmp;
    }

    private static ClassPathResource getClasspathResource(String location) {
        ClassPathResource classPathResource = new ClassPathResource(location);
        String absPath;
        try {
            absPath = classPathResource.getURI().toString();
            boolean isJar = absPath.contains("jar");
            boolean isWar = absPath.contains("war");
            if (isJar)
                log.warn("Reloadable properties [{}] found in jar by path [{}]", location, absPath);
            if (isWar)
                log.warn("Reloadable properties [{}] found in war by path [{}]", location, absPath);
            else
                log.debug("Reloadable properties [{}] found by path [{}]", location, absPath);
            return classPathResource;
        } catch (IOException e) {
            log.error("Malformed classpath location [{}] for reloadable properties", location);
        }
        return null;
    }

    private static FileSystemResource getFilesystemResource(String location) {
        File file = new File(location);
        return new FileSystemResource(file);
    }
}
