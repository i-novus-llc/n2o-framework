package net.n2oapp.framework.api.metadata.local.context;

import net.n2oapp.framework.api.metadata.global.view.page.N2oPage;
import net.n2oapp.framework.api.metadata.local.GlobalMetadataProvider;
import net.n2oapp.properties.StaticProperties;

/**
 * User: operhod
 * Date: 27.11.13
 * Time: 14:42
 */
@Deprecated
public abstract class PageContext<P extends CompileContext> extends BaseCompileContext<N2oPage, P> {

    private static final int DEFAULT_MAX_NESTING = 7;

    public PageContext(P parentContext, String pageId) throws OutOfRangeException {
        super(parentContext, pageId);
        checkNested(parentContext);
    }

    public PageContext(P parentContext, ContextMetadataProvider<N2oPage,P> metadataProvider) throws OutOfRangeException {
        super(parentContext, metadataProvider);
        checkNested(parentContext);
    }

    protected PageContext(String pageId) {
        super(null, pageId);
    }

    public boolean isOpenFromField() {
        return false;
    }

    public String getResultContainerId() {
        return null;
    }

    public String getFilterContainerId() {
        return null;
    }

    public boolean getResolveModal() {
        return false;
    }


    private void checkNested(P parentContext) throws OutOfRangeException {
        if (parentContext != null) {
            setNesting(parentContext.getNesting() + 1);
            if (isOutOfRange()) {
                throw new OutOfRangeException();
            }
        }
    }

    @Override
    public Class<N2oPage> getMetadataClassByContext() {
        return N2oPage.class;
    }

    @Override
    public N2oPage getMetadata(GlobalMetadataProvider compiler, String... tokens) {
        return super.getMetadata(compiler, tokens);
    }

    public boolean isModal() {
        return false;
    }

    private boolean isOutOfRange() {
        Integer maxNesting = StaticProperties.getInteger("n2o.context.compile.max.nesting");
        return getNesting() > (maxNesting == null ?  DEFAULT_MAX_NESTING : maxNesting);
    }
}
