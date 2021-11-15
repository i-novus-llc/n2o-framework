package net.n2oapp.framework.api.metadata.meta;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.control.SubmitOn;
import net.n2oapp.framework.api.metadata.meta.widget.RequestMethod;

import java.util.Map;

/**
 * Информация об URL адресе для получения данных виджета
 */
@Getter
@Setter
public class ClientDataProvider implements Compiled {
    @JsonProperty
    private String url;
    @JsonProperty
    private Map<String, ModelLink> pathMapping;
    @JsonProperty
    private Map<String, ModelLink> queryMapping;
    @JsonProperty
    private Map<String, ModelLink> headersMapping;
    @JsonProperty
    private Map<String, ModelLink> formMapping;
    @JsonProperty
    private RequestMethod method;
    @JsonProperty
    private String quickSearchParam;
    @JsonProperty
    private Boolean optimistic;
    @JsonProperty
    private Boolean submitForm;
    @JsonProperty
    private Integer size;
    @JsonProperty
    private SubmitOn autoSubmitOn;
}
