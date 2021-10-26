package net.n2oapp.framework.api.metadata.meta.action;

import net.n2oapp.framework.api.metadata.global.view.action.control.Target;
import net.n2oapp.framework.api.metadata.meta.ModelLink;

import java.util.Map;

public interface ActionAware {
    Action getAction();
    void setAction(Action action);
    String getUrl();
    void setUrl(String url);
    Target getTarget();
    void setTarget(Target target);
    Map<String, ModelLink> getPathMapping();
    void setPathMapping(Map<String, ModelLink> pathMapping);
    Map<String, ModelLink> getQueryMapping();
    void setQueryMapping(Map<String, ModelLink> queryMapping);
}
