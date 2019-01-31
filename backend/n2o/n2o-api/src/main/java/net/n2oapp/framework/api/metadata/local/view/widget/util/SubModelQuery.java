package net.n2oapp.framework.api.metadata.local.view.widget.util;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.Compiled;

/**
 * Информация о вложенных моделях выборки
 */
@Getter
@Setter
public class SubModelQuery implements Compiled {

    private String subModel;
    private String queryId;
    private String valueFieldId;
    private String labelFieldId;
    private Boolean multi;

    public SubModelQuery(String subModel, String queryId, String valueFieldId, String labelFieldId, Boolean multi) {
        this.subModel = subModel;
        this.queryId = queryId;
        this.valueFieldId = valueFieldId;
        this.labelFieldId = labelFieldId;
        this.multi = multi;
    }

    public SubModelQuery(String queryId) {
        this.queryId = queryId;
    }
}
