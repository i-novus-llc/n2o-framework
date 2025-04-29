package net.n2oapp.framework.api.metadata.global.view.page.datasource;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.ReduxModelEnum;
import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.datasource.Submittable;
import net.n2oapp.framework.api.metadata.global.dao.N2oPreFilter;

/**
 * Исходная модель источника данных, получающего данные из другого источника данных
 */
@Getter
@Setter
public class N2oInheritedDatasource extends N2oDatasource implements Submittable {

    private String sourceDatasource;
    private ReduxModelEnum sourceModel;
    private String sourceFieldId;
    private String fetchValue;
    private Submit submit;
    private N2oPreFilter[] filters;

    @Getter
    @Setter
    public static class Submit implements Source {
        private Boolean auto;
        private ReduxModelEnum model;
        private String targetDatasource;
        private ReduxModelEnum targetModel;
        private String targetFieldId;
        private String submitValue;
    }

}
