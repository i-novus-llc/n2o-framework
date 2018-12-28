package net.n2oapp.framework.api.metadata.local.context;

import net.n2oapp.framework.api.metadata.global.view.page.N2oPage;

/**
 * Контекст корневой страницы
 */
public class RootPageContext extends PageContext<CompileContext> {
    private String virtualPageId;

    public RootPageContext(String pageId) {
        super(pageId);
    }

    public RootPageContext(String pageId, ContextMetadataProvider<N2oPage, CompileContext> metadataProvider) throws OutOfRangeException {
        super(null, metadataProvider);
        this.virtualPageId = pageId;
    }

    @Override
    protected String getIdByContext() {
        return virtualPageId != null ? virtualPageId : getMetadataId();
    }
}
