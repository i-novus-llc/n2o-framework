package net.n2oapp.framework.api.metadata.meta.action.link;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.global.view.action.control.Target;
import net.n2oapp.framework.api.metadata.local.util.StrictMap;
import net.n2oapp.framework.api.metadata.meta.ModelLink;
import net.n2oapp.framework.api.metadata.meta.action.AbstractAction;
import net.n2oapp.framework.api.metadata.meta.action.ActionPayload;
import net.n2oapp.framework.api.metadata.meta.action.LinkAction;
import net.n2oapp.framework.api.metadata.meta.saga.MetaSaga;

import java.util.Map;

/**
 * Клиентская модель <a>
 */
@Getter
@Setter
public class LinkActionImpl extends AbstractAction<ActionPayload, MetaSaga> implements LinkAction {

    private String objectId;
    private String operationId;
    private String pageId;

    private String url;
    private Target target;
    private Map<String, ModelLink> pathMapping = new StrictMap<>();
    private Map<String, ModelLink> queryMapping = new StrictMap<>();

    public LinkActionImpl() {
        super(null, null);
    }
}
