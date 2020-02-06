package net.n2oapp.framework.api.metadata.meta.widget;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.meta.BindLink;

import java.util.Map;

/**
 * Информация об URL адресе для получения данных виджета
 */
@Getter
@Setter
public class WidgetDataProvider implements Compiled {
    @JsonProperty
    private String url;
    @JsonProperty
    private Map<String, BindLink> pathMapping;
    @JsonProperty
    private Map<String, BindLink> queryMapping;
    @JsonProperty
    private RequestMethod method;
    @JsonProperty
    private String quickSearchParam;
    @JsonProperty
    private Boolean optimistic;
}
