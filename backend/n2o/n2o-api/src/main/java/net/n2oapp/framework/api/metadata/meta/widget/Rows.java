package net.n2oapp.framework.api.metadata.meta.widget;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.aware.JsonPropertiesAware;

import java.io.Serializable;
import java.util.Map;

/**
 * Свойства строк для списковых виджетов
 */
@Getter
@Setter
public class Rows implements JsonPropertiesAware, Serializable {
    private Map<String, Object> properties;
}
