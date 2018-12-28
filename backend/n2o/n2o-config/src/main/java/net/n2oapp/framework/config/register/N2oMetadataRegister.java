package net.n2oapp.framework.config.register;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.SourceMetadata;
import net.n2oapp.framework.api.register.SourceInfo;
import net.n2oapp.framework.api.register.MetadataRegister;
import net.n2oapp.framework.config.reader.ReferentialIntegrityViolationException;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Реализация реестра метаданных
 */
public class N2oMetadataRegister implements MetadataRegister {
    private Map<MetaKey, SourceInfo> register = new ConcurrentHashMap<>();


    @Override
    public SourceInfo get(String id, Class<? extends SourceMetadata> sourceClass) {
        MetaKey key = new MetaKey(id.contains("?") ? id.substring(0, id.indexOf("?")) : id, sourceClass);
        if (!register.containsKey(key)) {
            throw new ReferentialIntegrityViolationException(id, sourceClass);
        }
        return register.get(key);
    }

    @Override
    public List<SourceInfo> find(Class<? extends SourceMetadata> sourceClass) {
        return register.entrySet().stream()
                .filter(e -> e.getKey().sourceClass.equals(sourceClass))
                .map(Map.Entry::getValue).collect(Collectors.toList());
    }

    @Override
    public List<SourceInfo> find(Predicate<SourceInfo> criteria) {
        return register.values().stream()
                .filter(criteria).collect(Collectors.toList());
    }

    @Override
    public <I extends SourceInfo> List<I> find(Predicate<I> criteria, Class<I> infoClass) {
        return register.values().stream().filter(infoClass::isInstance).map(infoClass::cast)
                .filter(criteria).collect(Collectors.toList());
    }

    @Override
    public boolean contains(String id, Class<? extends SourceMetadata> sourceClass) {
        return register.containsKey(new MetaKey(id.contains("?") ? id.substring(0, id.indexOf("?")) : id, sourceClass));
    }

    @Override
    public <I extends SourceInfo> void add(I info) {
        MetaKey key = new MetaKey(info.getId(), info.getBaseSourceClass());
        register.put(key, info);
    }

    @Override
    public <I extends SourceInfo> void addAll(Collection<I> infoList) {
        infoList.forEach(this::add);
    }

    @Override
    public void remove(String id, Class<? extends SourceMetadata> sourceClass) {
        register.remove(new MetaKey(id.contains("?") ? id.substring(0, id.indexOf("?")) : id, sourceClass));
    }

    @Override
    public <I extends SourceInfo> void update(I info) {

    }

    @Override
    public void clearAll() {
        register.clear();
    }

    static class MetaKey implements Serializable {
        private String id;
        private Class<? extends Source> sourceClass;

        MetaKey(String id, Class<? extends Source> sourceClass) {
            if (id == null)
                throw new IllegalArgumentException("Id must not be null");
            if (sourceClass == null)
                throw new IllegalArgumentException("SourceClass must not be null");
            this.id = id;
            this.sourceClass = sourceClass;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof MetaKey)) return false;
            MetaKey compare = (MetaKey) o;
            return id.equalsIgnoreCase(compare.id) &&
                    (compare.sourceClass.equals(sourceClass) || compare.sourceClass.isAssignableFrom(sourceClass));
        }

        @Override
        public int hashCode() {
            return Objects.hash(id.toLowerCase());
        }

        @Override
        public String toString() {
            return id + "." + sourceClass.getSimpleName();
        }
    }
}
