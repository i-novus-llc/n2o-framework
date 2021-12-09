package net.n2oapp.framework.api.metadata.meta.widget;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.Compiled;

/**
 * Базовая клиентская модель компонента виджета
 */
@Getter
@Setter
public abstract class WidgetComponent implements Compiled {
    @JsonProperty("fetchOnInit")
    private Boolean fetchOnInit;
    @JsonProperty("autoFocus")
    private Boolean autoFocus = false;
}
