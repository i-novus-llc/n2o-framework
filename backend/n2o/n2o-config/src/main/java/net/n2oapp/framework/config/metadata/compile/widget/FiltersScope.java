package net.n2oapp.framework.config.metadata.compile.widget;


import lombok.AllArgsConstructor;
import lombok.Getter;
import net.n2oapp.framework.api.metadata.meta.Filter;

import java.util.List;

/**
 * Информация по фильтрам
 */
@Getter
@AllArgsConstructor
public class FiltersScope  {
    List<Filter> filters;

}
