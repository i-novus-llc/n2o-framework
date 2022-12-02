package net.n2oapp.framework.api.metadata.meta.widget;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.aware.JsonPropertiesAware;

import java.util.Map;

/**
 * Свойства строк для списковых виджетов
 */
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Rows implements JsonPropertiesAware, Compiled {
    private Map<String, Object> properties;
}
