package net.n2oapp.framework.api.metadata.meta;

import lombok.*;
import net.n2oapp.framework.api.metadata.Compiled;

/**
 * Скомпилированная модель фильтра
 */
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@ToString
public class Filter implements Compiled {

    public Filter(String filterId, String param, ModelLink link) {
        this.filterId = filterId;
        this.param = param;
        this.link = link;
    }

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
