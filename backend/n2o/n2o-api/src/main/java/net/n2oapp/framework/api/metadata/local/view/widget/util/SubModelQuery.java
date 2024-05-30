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

    // use for value in multiset
    private String multisetPrefix;
    private String subModel;
    private String queryId;
    @Deprecated //"id" always
    private String valueFieldId;
    private String labelFieldId;
    //multi выбор
    private boolean multi = false;
    private List<Map<String, Object>> options;

    public SubModelQuery(String subModel, String queryId, String valueFieldId, String labelFieldId, boolean multi, List<Map<String, Object>> options) {
        this.subModel = subModel;
        this.queryId = queryId;
        this.valueFieldId = valueFieldId;
        this.labelFieldId = labelFieldId;
        this.multi = multi;
        this.options = options;
    }

    public SubModelQuery(String multisetPrefix, String subModel, String queryId, String valueFieldId, String labelFieldId, boolean multi, List<Map<String, Object>> options) {
        this(subModel, queryId, valueFieldId, labelFieldId, multi, options);
        this.multisetPrefix = multisetPrefix;
    }

    public SubModelQuery(String queryId) {
        this.queryId = queryId;
    }

    public String getFullName(){
        return multisetPrefix == null ? subModel : multisetPrefix + "." + subModel;
    }
}
