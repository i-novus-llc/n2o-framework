package net.n2oapp.framework.api.metadata.meta.action.open_drawer;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.meta.action.AbstractAction;
import net.n2oapp.framework.api.metadata.meta.saga.MetaSaga;

/**
 * Клиентская модель открытия drawer окна
 */
@Getter
@Setter
public class OpenDrawer extends AbstractAction<OpenDrawerPayload, MetaSaga> {

    private String objectId;
    private String operationId;
    private String pageId;
    private Boolean backdrop;
    private String width;
    private String height;
    private String placement;
    private String level;
    private Boolean backdropClosable;

    public OpenDrawer() {
        super(new OpenDrawerPayload(), null);
    }
}
