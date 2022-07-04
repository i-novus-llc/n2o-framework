package net.n2oapp.framework.api.metadata.global.view.page.datasource;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.ReduxModel;
import net.n2oapp.framework.api.metadata.Source;

/**
 * Исходная модель источника данных, получающего данные из другого источника данных
 */
@Getter
@Setter
public class N2oInheritedDatasource extends N2oDatasource {

    private String sourceDatasource;
    private ReduxModel sourceModel;
    private String sourceFieldId;
    private Submit submit;

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
