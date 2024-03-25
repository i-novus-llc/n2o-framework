package net.n2oapp.framework.api.metadata.meta.control;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.meta.ClientDataProvider;

/**
 * Клиентская модель зависимости обновления модели для поля ввода
 */
@Getter
@Setter
public class FetchValueDependency extends ControlDependency {
    @JsonProperty
    private ClientDataProvider dataProvider;
    @JsonProperty
    private String valueFieldId;
}
