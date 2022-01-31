package net.n2oapp.framework.api.metadata.global.dao;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.ReduxModel;

/**
 * Модель предустановленных параметров тела запроса
 */
@Getter
@Setter
@NoArgsConstructor
public class N2oFormParam extends N2oParam {
    /**
     * Идентификатор параметра
     */
    private String id;

    public N2oFormParam(String id, String value, String refWidgetId, ReduxModel refModel, String refPageId) {
        this.id = id;
        setValue(value);
        setRefWidgetId(refWidgetId);
        setModel(refModel);
        setRefPageId(refPageId);
    }

    // необходим для обратной совместимости после замены name на id
    @Deprecated
    public String getName() {
        return getId();
    }

    // необходим для обратной совместимости после замены name на id
    @Deprecated
    public void setName(String name) {
        this.id = name;
        super.setName(name);
    }
}
