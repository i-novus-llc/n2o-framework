package net.n2oapp.framework.api.metadata.header;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.SourceMetadata;
import net.n2oapp.framework.api.metadata.global.N2oMetadata;
import net.n2oapp.framework.api.metadata.global.view.action.control.Target;

/**
 * Панель поиска
 */
@Getter
@Setter
public class N2oSearchBar extends N2oMetadata {

    private String queryId; //запрос за данными, чтобы отобразить их в выпадающем popup
    private String filterFieldId; //id поля фильтра
    private String urlFieldId; // маппинг url
    private String labelFieldId; // маппинг label
    private String iconFieldId; // мапиинг иконки
    private String descriptionFieldId; // маппинг описания
    private Target advancedTarget;
    private String advancedUrl; //запрос за данными, чтобы отобразить их в открывшейся странице результатов поиска
    private String advancedParam; //параметр фильтра

    @Override
    public String getPostfix() {
        return "search";
    }

    @Override
    public Class<? extends SourceMetadata> getSourceBaseClass() {
        return N2oSearchBar.class;
    }
}
