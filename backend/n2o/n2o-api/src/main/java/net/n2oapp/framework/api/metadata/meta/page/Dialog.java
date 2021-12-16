package net.n2oapp.framework.api.metadata.meta.page;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.meta.toolbar.Toolbar;

/**
 * Клиентская модель диалога подтверждения действия
 */
@Getter
@Setter
public class Dialog implements Compiled {
    /**
     * Размер диалога (sm,lg)
     */
    @JsonProperty
    private String size;
    /**
     * Заголовок диалога
     */
    @JsonProperty
    private String title;

    /**
     * Текст внутри диалога
     */
    @JsonProperty("text")
    private String description;

    /**
     * Тулбар
     */
    @JsonProperty
    private Toolbar toolbar;

}
