package net.n2oapp.framework.api.metadata.meta.control;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.meta.widget.WidgetDataProvider;

import java.util.ArrayList;
import java.util.List;

/**
 * Клиентская молель зависимости обновления модели для поля ввода
 */
@Getter
@Setter
public class FetchValueDependency extends ControlDependency {
    @JsonProperty
    private WidgetDataProvider dataProvider;
    @JsonProperty
    private String valueFieldId;
}
