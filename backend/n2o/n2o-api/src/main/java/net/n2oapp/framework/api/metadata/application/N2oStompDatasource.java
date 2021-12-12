package net.n2oapp.framework.api.metadata.application;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * Исходная модель STOMP-источника данных
 */
@Getter
@Setter
public class N2oStompDatasource extends N2oDatasource {

    private String destination;
    private Value[] values;

    @Getter
    @Setter
    public static class Value {
        private Map<String, Object> attribute;
    }
}
