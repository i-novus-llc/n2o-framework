package net.n2oapp.framework.api.metadata.header;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.aware.NamespaceUriAware;
import net.n2oapp.framework.api.metadata.global.view.action.control.Target;
import org.jdom.Namespace;

/**
 * Панель поиска
 */
@Getter
@Setter
public class N2oSearchBar implements Source, NamespaceUriAware {

    static Namespace NAMESPACE = Namespace.getNamespace("http://n2oapp.net/framework/config/schema/search-1.0");

    private String queryId;
    private String filterFieldId;
    private String urlFieldId;
    private String labelFieldId;
    private String iconFieldId;
    private String descriptionFieldId;
    private Target advancedTarget;
    private String advancedUrl;
    private String advancedParam;

    @Override
    public Namespace getNamespace() {
        return NAMESPACE;
    }

    @Override
    public String getNamespaceUri() {
        return NAMESPACE.getURI();
    }
}
