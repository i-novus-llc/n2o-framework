package net.n2oapp.framework.config.register;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.register.ComponentTypeRegister;
import net.n2oapp.framework.config.reader.MetaTypeNotFoundException;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Реализация реестра типов метаданных для выгрузки в json
 */
public class N2oComponentTypeRegister implements ComponentTypeRegister {

    private final Map<String, Class<? extends Source>> typeClassMap = new ConcurrentHashMap<>();
    private final Map<Class<? extends Source>, String> classTypeMap = new ConcurrentHashMap<>();

    @Override
    public void add(String type, Class<? extends Source> clazz) {
        typeClassMap.put(type, clazz);
        classTypeMap.put(clazz, type);
    }

    @Override
    public void addAll(Map<String, Class<? extends Source>> componentTypes) {
        componentTypes.forEach(this::add);
    }

    @Override
    public String getByClass(Class<? extends Source> clazz) {
        if (!classTypeMap.containsKey(clazz))
            throw new MetaTypeNotFoundException(clazz);
        return classTypeMap.get(clazz);
    }

    @Override
    public Class<? extends Source> getByType(String type) {
        if (!typeClassMap.containsKey(type))
            throw new MetaTypeNotFoundException(type);
        return typeClassMap.get(type);
    }

    @Override
    public void clearAll() {
        typeClassMap.clear();
        classTypeMap.clear();
    }
}
