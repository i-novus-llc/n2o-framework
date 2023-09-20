package net.n2oapp.framework.api.metadata.meta.widget.table;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.meta.widget.toolbar.Group;

import java.util.List;

/**
 * Клиентская модель "Поведение при наведении на строку"
 */
@Getter
@Setter
public class RowOverlay implements Compiled {
    @JsonProperty
    private String className;
    @JsonProperty
    private List<Group> toolbar;
}
