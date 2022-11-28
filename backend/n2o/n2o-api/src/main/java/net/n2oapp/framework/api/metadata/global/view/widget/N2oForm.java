package net.n2oapp.framework.api.metadata.global.view.widget;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.SourceComponent;
import net.n2oapp.framework.api.metadata.control.Submit;
import net.n2oapp.framework.api.metadata.control.SubmitOn;
import net.n2oapp.framework.api.metadata.datasource.Submittable;
import net.n2oapp.framework.api.metadata.global.view.page.datasource.N2oStandardDatasource;

/**
 * Исходная модель виджета Форма
 */
@Getter
@Setter
public class N2oForm extends N2oWidget implements Submittable {
    private SourceComponent[] items;
    private FormMode mode;
    private Boolean prompt;
    @Deprecated
    private Submit submit;

    @Deprecated
    public void adapterV4() {
        super.adapterV4();
        if (getQueryId() != null) {
            getDatasource().setSize(1);
        }
        if (getSubmit() != null) {
            if (getDatasource() == null)
                setDatasource(new N2oStandardDatasource());
            getDatasource().setSubmit(getSubmit());
            if (getDatasource().getSubmit().getRefreshWidgetId() != null) {
                getDatasource().getSubmit().setRefreshDatasourceIds(
                        new String[]{getDatasource().getSubmit().getRefreshWidgetId()});//не учитываются datasource у виджета в 7.19
            } else {
                getDatasource().getSubmit().setRefreshDatasourceIds(
                        new String[]{getId()});//не учитываются datasource у виджета в 7.19
            }
            getDatasource().getSubmit().setMessageWidgetId(getId());
            getDatasource().getSubmit().setSubmitOn(SubmitOn.change);
        }
    }
}
