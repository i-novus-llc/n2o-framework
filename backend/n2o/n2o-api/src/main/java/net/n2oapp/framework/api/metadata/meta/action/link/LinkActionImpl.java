package net.n2oapp.framework.api.metadata.meta.action.link;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.global.view.action.control.Target;
import net.n2oapp.framework.api.metadata.meta.ModelLink;
import net.n2oapp.framework.api.metadata.meta.action.AbstractAction;
import net.n2oapp.framework.api.metadata.meta.action.LinkAction;
import net.n2oapp.framework.api.metadata.meta.saga.MetaSaga;

import java.util.Map;

/**
 * Клиентская модель действия перехода по ссылке
 */
@Getter
@Setter
public class LinkActionImpl extends AbstractAction<LinkActionPayload, MetaSaga> implements LinkAction {

    private String objectId;
    private String operationId;
    private String pageId;

    public LinkActionImpl() {
        super(new LinkActionPayload(), null);
    }

    @Override
    public String getUrl() {
        return getPayload().getUrl();
    }

    @Override
    public void setUrl(String url) {
        getPayload().setUrl(url);
    }

    @Override
    public Target getTarget() {
        return getPayload().getTarget();
    }

    @Override
    public void setTarget(Target target) {
        getPayload().setTarget(target);
    }

    @Override
    public Map<String, ModelLink> getPathMapping() {
        return getPayload().getPathMapping();
    }

    @Override
    public void setPathMapping(Map<String, ModelLink> pathMapping) {
        getPayload().setPathMapping(pathMapping);
    }

    @Override
    public Map<String, ModelLink> getQueryMapping() {
        return getPayload().getQueryMapping();
    }

    @Override
    public void setQueryMapping(Map<String, ModelLink> queryMapping) {
        getPayload().setQueryMapping(queryMapping);
    }

    public Boolean getRestore() {
        return getPayload().getRestore();
    }

    public void setRestore(Boolean restore) {
       getPayload().setRestore(restore);
    }

}
