package net.n2oapp.framework.api.metadata.header;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.global.view.action.control.Target;

/**
 * Панель поиска
 */
@Getter
@Setter
public class N2oSearchBar implements Source {
    private String queryId;
    private String filterFieldId;
    private String urlFieldId;
    private String labelFieldId;
    private String iconFieldId;
    private String descriptionFieldId;
    private Target target;
}
