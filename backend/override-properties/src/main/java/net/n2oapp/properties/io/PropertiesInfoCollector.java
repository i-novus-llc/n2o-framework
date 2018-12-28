package net.n2oapp.properties.io;

import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author V. Alexeev.
 */
public class PropertiesInfoCollector {

    private final static String GROUP_PREFIX = "##";
    private final static String NAME_PREFIX = "#";

    private final String locationPattern;
    private final String encode;
    private Map<String, List<PropertyInfo>> data;
    private final AtomicBoolean init = new AtomicBoolean(false);

    public PropertiesInfoCollector(String locationPattern) {
        this.locationPattern = locationPattern;
        this.encode = "UTF-8";
    }

    public PropertiesInfoCollector(String encode, String locationPattern) {
        this.encode = encode;
        this.locationPattern = locationPattern;
    }

    public Map<String, List<PropertyInfo>> getPropertyInfoMap() {
        lazyInit();
        return data;
    }

    public String getGroup(String property) {
        return getPropertyInfoMap().entrySet().stream()
                .filter(e -> e.getValue().stream().filter(p -> p.key.equals(property)).findFirst().isPresent())
                .findFirst().map(Map.Entry::getKey).orElse(null);
    }

    public PropertyInfo getInfo(String group, String key) {
        return group != null ? getPropertyInfoMap().getOrDefault(group, Collections.emptyList())
                .stream().filter(info -> info.key.equals(key)).findFirst().orElseGet(() -> new PropertyInfo(key)) : new PropertyInfo(key);
    }

    private void lazyInit() {
        if (!init.get()) {
            synchronized (init) {
                if (!init.get()) {
                    try {
                        data = calculate();
                    } catch (IOException e) {
                        throw new IllegalStateException(e);
                    }
                    init.set(true);
                }
            }
        }
    }

    private Map<String, List<PropertyInfo>> calculate() throws IOException {
        Map<String, List<PropertiesInfoCollector.PropertyInfo>> propertyMap = new HashMap<>();
        PathMatchingResourcePatternResolver pathPatternResolver = new PathMatchingResourcePatternResolver();
        Resource[] resources = pathPatternResolver.getResources(locationPattern);
        if (resources == null || resources.length == 0) {
            return Collections.unmodifiableMap(new HashMap<>());
        }
        Arrays.stream(resources).map(this::retrieve)
                .forEach(file -> {
                    final String[] group = {""};
                    final String[] name = {null};
                    final String[] description = {""};
                    file.forEach(str -> {
                        for (String line : str.split("\n")) {
                            line = clean(line);
                            if (!line.isEmpty()) {
                                if (isGroup(line)) {
                                    group[0] = line.replaceFirst("##", "").trim();
                                } else if (name[0] == null && isNameOrDescription(line)) {
                                    name[0] = line;
                                } else if (name[0] != null && isNameOrDescription(line)) {
                                    description[0] = description[0].concat(line).concat("\n");
                                } else { //is val
                                    propertyMap.computeIfAbsent(group[0], key -> new ArrayList<>())
                                            .add(new PropertiesInfoCollector.PropertyInfo(getKey(line), group[0], name[0], description[0]));
                                    name[0] = null;
                                    description[0] = "";
                                }
                            }
                        }
                    });
                });

        Map<String, List<PropertiesInfoCollector.PropertyInfo>> propertyMapNotModified = new ConcurrentHashMap<>();
        propertyMap.forEach((k, v) -> propertyMapNotModified.put(k, Collections.unmodifiableList(v)));
        return Collections.unmodifiableMap(propertyMapNotModified);
    }

    private List<String> retrieve(Resource resource) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream(), encode))) {
            List<String> lines = new ArrayList<>();
            String str;
            while ((str = reader.readLine()) != null)
                lines.add(str.concat("\n"));
            return lines;
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    private boolean isGroup(String str) {
        return str.startsWith(GROUP_PREFIX);
    }

    private boolean isNameOrDescription(String str) {
        return str.startsWith(NAME_PREFIX);
    }

    private String getKey(String str) {
        return str.split("[\\s=]")[0];
    }

    private String clean(String str) {
        return str.replaceFirst("\\s*", "");
    }

    public static class PropertyInfo {
        public final String key;
        public final String group;
        public final String name;
        public final String description;

        public PropertyInfo(String key) {
            this.key = key;
            this.group = null;
            this.name = null;
            this.description = null;
        }

        public PropertyInfo(String key, String group, String name, String description) {
            this.key = key;
            this.group = group;
            this.name = name != null ? name.replaceFirst("#", "").trim() : null;
            this.description = description != null && !description.isEmpty() ?
                    description.replaceFirst("#", "").replaceAll("\\n#", "\n").replaceAll("\\s+", " ").trim() : null;
        }
    }

}
