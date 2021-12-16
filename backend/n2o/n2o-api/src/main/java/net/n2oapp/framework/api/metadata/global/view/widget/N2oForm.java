package net.n2oapp.framework.api.metadata.global.view.widget;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.exception.N2oException;
import net.n2oapp.framework.api.metadata.SourceComponent;
import net.n2oapp.framework.api.metadata.control.Submit;
import net.n2oapp.framework.api.metadata.global.view.page.N2oDatasource;

/**
 * Исходная модель виджета Форма
 */
@Getter
@Setter
public class N2oForm extends N2oWidget {
    private String modalWidth;
    private String layout;
    private SourceComponent[] items;
    private FormMode mode;
    private Boolean prompt;
    @Deprecated
    private Submit submit;

    @Deprecated
    public void adapterV5() {
        if (getSubmit() != null) {
            if (getDatasource() == null)
                setDatasource(new N2oDatasource());
            getDatasource().setSubmit(getSubmit());
            if (getDatasource().getSubmit().getRefreshWidgetId() != null) {
                getDatasource().getSubmit().setRefreshDatasources(
                        new String[]{getDatasource().getSubmit().getRefreshWidgetId()});//не учитываются datasource у виджета в 7.19
            } else {
                getDatasource().getSubmit().setRefreshDatasources(
                        new String[]{getId()});//не учитываются datasource у виджета в 7.19
            }
            getDatasource().getSubmit().setMessageWidgetId(getId());
        }
    }
}
