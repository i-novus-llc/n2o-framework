package net.n2oapp.framework.api.register;

import java.util.Map;

/**
 * Реестр типов сырых метаданных для выгрузки в json
 */
public interface ComponentTypeRegister {

    void add(String type, Class clazz);

    void addAll(Map<String, Class> componentTypes);

    String getByClass(Class clazz);

    Class getByType(String type);

    void clearAll();
}
