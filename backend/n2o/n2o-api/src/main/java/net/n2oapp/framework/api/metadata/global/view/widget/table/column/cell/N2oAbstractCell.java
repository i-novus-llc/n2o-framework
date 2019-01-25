package net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.aware.CssClassAware;
import net.n2oapp.framework.api.metadata.event.action.N2oAction;
import net.n2oapp.framework.api.metadata.meta.action.Action;

/**
 * Абстрактная ячейка
 */
@Getter
@Setter
public abstract class N2oAbstractCell implements N2oCell, CssClassAware {
    @JsonProperty
    private String id;
    @JsonProperty
    private String fieldKey;
    @JsonProperty
    private String src;
    private String namespaceUri;
    @JsonProperty("className")
    private String cssClass;
    private String visible;
    @JsonProperty("visible")
    private Object jsonVisible;
}
