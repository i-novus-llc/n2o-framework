package net.n2oapp.framework.api.metadata.meta.region.scrollspy;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.Compiled;

import java.util.List;

/**
 * Клиентская модель элемента scrollspy-региона
 */
@Getter
@Setter
public class SingleScrollspyElement extends ScrollspyElement {

    @JsonProperty
    private List<Compiled> content;
}
