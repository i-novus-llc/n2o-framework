package net.n2oapp.framework.api.metadata.meta;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.Compiled;

/**
 * Скомпилированная модель фильтра
 */
@Getter
@Setter
public class Filter implements Compiled {
    /**
     * Параметр запроса фильтра
     */
    private String param;
    /**
     * Идентификатор фильтра в выборке
     */
    private String filterId;
    /**
     * Значение фильтра: либо ссылка на поле, либо константа
     */
    private ModelLink link;
    /**
     * Может ли значение фильтра изменяться после отрисовки страницы?
     */
    private Boolean routable;
}
