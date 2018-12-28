package net.n2oapp.framework.config.register;

import net.n2oapp.framework.api.metadata.SourceMetadata;
import net.n2oapp.framework.api.register.MetaType;
import net.n2oapp.framework.api.register.SourceTypeRegister;
import net.n2oapp.framework.config.reader.MetaTypeNotFoundException;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Реализация реестра типов метаданных
 */
public class N2oSourceTypeRegister implements SourceTypeRegister {
    private Map<String, MetaType> register = new ConcurrentHashMap<>();

    @Override
    public void add(MetaType metaType) {
        register.put(metaType.getSourceType(), metaType);
    }

    @Override
    public void addAll(Collection<MetaType> metaType) {
        metaType.forEach(this::add);
    }

    @Override
    public MetaType get(String sourceType) {
        if (!register.containsKey(sourceType))
            throw new MetaTypeNotFoundException(sourceType);
        return register.get(sourceType);
    }

    @Override
    public MetaType get(Class<? extends SourceMetadata> sourceClass) {
        for (MetaType metaType : register.values()) {
            if (metaType.getBaseSourceClass().equals(sourceClass))
                return metaType;
        }
        for (MetaType metaType : register.values()) {
            if (metaType.getBaseSourceClass().isAssignableFrom(sourceClass))
                return metaType;
        }
        throw new MetaTypeNotFoundException(sourceClass);
    }

    @Override
    public void clearAll() {
        register.clear();
    }


}
