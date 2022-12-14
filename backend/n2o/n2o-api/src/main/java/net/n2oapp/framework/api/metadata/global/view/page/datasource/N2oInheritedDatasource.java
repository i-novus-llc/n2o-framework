package net.n2oapp.framework.api.metadata.global.view.page.datasource;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.ReduxModel;
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
    private ReduxModel sourceModel;
    private String sourceFieldId;
    private Submit submit;
    private N2oPreFilter[] filters;

    @Getter
    @Setter
    public static class Submit implements Source {
        private Boolean auto;
        private ReduxModel model;
        private String targetDatasource;
        private ReduxModel targetModel;
        private String targetFieldId;
    }

}
