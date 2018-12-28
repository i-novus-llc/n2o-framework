package net.n2oapp.framework.api.metadata.local.context;

import net.n2oapp.framework.api.metadata.event.action.N2oAction;
import net.n2oapp.framework.api.metadata.global.dao.N2oPreFilter;
import net.n2oapp.framework.api.metadata.global.view.widget.N2oWidget;

import java.util.*;

@Deprecated
public abstract class WidgetContext<P extends CompileContext> extends BaseCompileContext<N2oWidget, P> {

    protected boolean isResult;//resolve запись этого контейнера ждёт инициатор страницы для установки себе
    protected Map<String, String> queryFieldNamesMap = new HashMap<>();
    protected String dependsContainerId;
    protected List<N2oPreFilter> preFilters = Collections.emptyList();
    private Map<String, N2oAction> events = new HashMap<>();

    protected WidgetContext(P parentContext, String id) {
        super(parentContext, id);
    }

    protected WidgetContext(P parentContext, N2oWidget metadata, ContextMetadataProvider<N2oWidget, P> metadataProvider) {
        super(parentContext, metadata, metadataProvider);
    }

    @Override
    protected String getIdByContext() {
        String parentId = getParentContextId() != null ? getParentContextId() + "." : "";
        return parentId + metadataId;
    }

    @Override
    public Class<N2oWidget> getMetadataClassByContext() {
        return N2oWidget.class;
    }

    public Map<String, String> getQueryFieldNamesMap() {
        return queryFieldNamesMap;
    }

    public boolean isResult() {
        return isResult;
    }

    public List<N2oPreFilter> getPreFilters() {
        return preFilters;
    }

    @Override
    public void clearCache() {
        super.clearCache();
    }
}
