package net.n2oapp.properties.io;

import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Pattern;

/**
 * @author V. Alexeev.
 */
public class PropertiesInfoCollector {

    private static final String GROUP_PREFIX = "##";
    private static final String NAME_PREFIX = "#";

    private final String locationPattern;
    private final String encode;
    private Map<String, List<PropertyInfo>> data;
    private final AtomicBoolean init = new AtomicBoolean(false);
    private static final Pattern GROUP_PREFIX_PATTERN = Pattern.compile(GROUP_PREFIX);
    private static final Pattern NAME_PREFIX_PATTERN = Pattern.compile(NAME_PREFIX);
    private static final Pattern LEADING_WHITESPACE_PATTERN = Pattern.compile("\\s*");
    private static final Pattern KEY_SEPARATOR_PATTERN = Pattern.compile("[\\s=]");
    private static final Pattern WHITESPACE_PATTERN = Pattern.compile("\\s+");

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
                .filter(e -> e.getValue().stream().anyMatch(p -> p.key.equals(property)))
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
        Map<String, List<PropertyInfo>> propertyMap = new HashMap<>();
        PathMatchingResourcePatternResolver pathResolver = new PathMatchingResourcePatternResolver();
        Resource[] resources = pathResolver.getResources(locationPattern);
        if (resources.length == 0)
            return Collections.unmodifiableMap(new HashMap<>());

        Arrays.stream(resources).map(this::retrieve)
                .forEach(file -> parseFile(file, propertyMap));

        Map<String, List<PropertyInfo>> unmodifiable = new ConcurrentHashMap<>();
        propertyMap.forEach((k, v) -> unmodifiable.put(k, Collections.unmodifiableList(v)));
        return Collections.unmodifiableMap(unmodifiable);
    }

    /**
     * Разобрать содержимое одного файла в набор свойств.
     */
    private void parseFile(List<String> file, Map<String, List<PropertyInfo>> propertyMap) {
        final String[] group = {""};
        final String[] name = {null};
        final String[] description = {""};
        for (String str : file) {
            for (String line : str.split("\n"))
                parseLine(clean(line), group, name, description, propertyMap);
        }
    }

    /**
     * Обработать одну строку properties-файла. Может обновить состояние
     * (group / name / description) и добавить PropertyInfo в map, если строка — значение.
     */
    private void parseLine(String line, String[] group, String[] name, String[] description,
                           Map<String, List<PropertyInfo>> propertyMap) {
        if (line.isEmpty())
            return;
        if (isGroup(line)) {
            group[0] = GROUP_PREFIX_PATTERN.matcher(line).replaceFirst("").trim();
        } else if (isNameOrDescription(line)) {
            if (name[0] == null)
                name[0] = line;
            else
                description[0] = description[0].concat(line).concat("\n");
        } else { //is val
            propertyMap.computeIfAbsent(group[0], key -> new ArrayList<>())
                    .add(new PropertyInfo(getKey(line), group[0], name[0], description[0]));
            name[0] = null;
            description[0] = "";
        }
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
        return KEY_SEPARATOR_PATTERN.split(str)[0];
    }

    private String clean(String str) {
        return LEADING_WHITESPACE_PATTERN.matcher(str).replaceFirst("");
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
            this.name = name != null ? NAME_PREFIX_PATTERN.matcher(name).replaceFirst("").trim() : null;
            this.description = description != null && !description.isEmpty()
                    ? WHITESPACE_PATTERN.matcher(
                            NAME_PREFIX_PATTERN.matcher(description).replaceFirst("").replace("\n#", "\n"))
                    .replaceAll(" ").trim()
                    : null;
        }
    }

}
