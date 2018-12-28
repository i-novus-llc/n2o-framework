package net.n2oapp.framework.api.metadata.local.context;


/**
 * Корневой контекст виджета
 */
@Deprecated
public class RootWidgetContext extends WidgetContext<CompileContext> {

    public RootWidgetContext(String id) {
        super(null, id);
    }

    @Override
    protected String getIdByContext() {
        return "/" + metadataId;
    }

}
