package net.n2oapp.framework.api.metadata.meta.action.link;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.meta.action.AbstractAction;

import java.util.Map;

/**
 * Клиентская модель <a>
 */
@Getter
@Setter
public class LinkAction extends AbstractAction<LinkActionOptions> {

    private Map<String, Object> properties;
    private String objectId;
    private String operationId;
    private String pageId;

    public LinkAction(LinkActionOptions options) {
        super(options);
    }
}
