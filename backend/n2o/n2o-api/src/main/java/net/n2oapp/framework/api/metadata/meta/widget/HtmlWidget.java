package net.n2oapp.framework.api.metadata.meta.widget;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * Клиентская модель HtmlWidget
 */
@Getter
@Setter
public class HtmlWidget extends Widget {
    @JsonProperty
    private String url;
}
