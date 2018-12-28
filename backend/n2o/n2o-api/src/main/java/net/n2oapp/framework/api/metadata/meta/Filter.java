package net.n2oapp.framework.api.metadata.meta;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.Compiled;

/**
 * Скимпилированная модель фильтра
 */
@Getter
@Setter
public class Filter implements Compiled {
    private String param;
    private String filterId;
    private ModelLink link;
    private Boolean reloadable;
}
