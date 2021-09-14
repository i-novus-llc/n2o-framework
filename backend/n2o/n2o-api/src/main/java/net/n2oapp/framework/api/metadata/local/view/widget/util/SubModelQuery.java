package net.n2oapp.framework.api.metadata.local.view.widget.util;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.Compiled;

import java.util.List;
import java.util.Map;

/**
 * Информация о вложенных моделях выборки
 */
@Getter
@Setter
public class SubModelQuery implements Compiled {

    private String subModel;
    private String queryId;
    @Deprecated //"id" always
    private String valueFieldId;
    private String labelFieldId;
    private Boolean multi;
    private List<Map<String, Object>> options;

    public SubModelQuery(String subModel, String queryId, String valueFieldId, String labelFieldId, Boolean multi, List<Map<String, Object>> options) {
        this.subModel = subModel;
        this.queryId = queryId;
        this.valueFieldId = valueFieldId;
        this.labelFieldId = labelFieldId;
        this.multi = multi;
        this.options = options;
    }

    public SubModelQuery(String queryId) {
        this.queryId = queryId;
    }
}
