package net.n2oapp.framework.config.register;

import net.n2oapp.framework.api.register.ComponentTypeRegister;
import net.n2oapp.framework.config.reader.MetaTypeNotFoundException;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Реализация реестра типов метаданных для выгрузки в json
 */
public class N2oComponentTypeRegister implements ComponentTypeRegister {

    private Map<String, Class> typeClassMap = new ConcurrentHashMap<>();
    private Map<Class, String> classTypeMap = new ConcurrentHashMap<>();

    @Override
    public void add(String type, Class clazz) {
        typeClassMap.put(type, clazz);
        classTypeMap.put(clazz, type);
    }

    @Override
    public void addAll(Map<String, Class> componentTypes) {
        componentTypes.forEach(this::add);
    }

    @Override
    public String getByClass(Class clazz) {
        if (!classTypeMap.containsKey(clazz))
            throw new MetaTypeNotFoundException(clazz);
        return classTypeMap.get(clazz);
    }

    @Override
    public Class getByType(String type) {
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
