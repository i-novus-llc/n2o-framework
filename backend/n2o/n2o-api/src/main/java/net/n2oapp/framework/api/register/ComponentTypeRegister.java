package net.n2oapp.framework.api.register;

import net.n2oapp.framework.api.metadata.Source;

import java.util.Map;

/**
 * Реестр типов исходных метаданных для выгрузки в json
 */
public interface ComponentTypeRegister {

    void add(String type, Class<? extends Source> clazz);

    void addAll(Map<String, Class<? extends Source>> componentTypes);

    String getByClass(Class<? extends Source> clazz);

    Class<? extends Source> getByType(String type);

    void clearAll();
}
