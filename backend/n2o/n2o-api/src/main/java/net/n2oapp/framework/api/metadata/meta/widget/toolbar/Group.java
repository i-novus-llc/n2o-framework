package net.n2oapp.framework.api.metadata.meta.widget.toolbar;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.aware.IdAware;
import java.util.List;

/**
 * Клиентская модель группы кнопок в меню
 */
@Getter
@Setter
public class Group implements Compiled, IdAware {
    @JsonProperty
    private String id;
    @JsonProperty
    private String className;
    @JsonProperty
    private List<AbstractButton> buttons;

    public Group(String id) {
        this.id = id;
    }

}
