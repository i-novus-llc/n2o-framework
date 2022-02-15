package net.n2oapp.framework.api.metadata.meta.region.scrollspy;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Клиентская модель элемента scrollspy-региона с вложенными элементами
 */
@Getter
@Setter
public class GroupScrollspyElement extends ScrollspyElement {

    @JsonProperty
    private List<ScrollspyElement> menu;
}
