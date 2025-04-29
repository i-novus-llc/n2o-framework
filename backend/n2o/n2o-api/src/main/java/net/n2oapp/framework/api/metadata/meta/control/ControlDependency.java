package net.n2oapp.framework.api.metadata.meta.control;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.Compiled;

import java.util.ArrayList;
import java.util.List;

/**
 * Клиентская модель зависимости между полями
 */
@Getter
@Setter
public class ControlDependency implements Compiled {
    @JsonProperty
    private ValidationTypeEnum type;
    @JsonProperty
    private List<String> on = new ArrayList<>();
    @JsonProperty
    private String expression;
    @JsonProperty
    private Boolean applyOnInit;

}
