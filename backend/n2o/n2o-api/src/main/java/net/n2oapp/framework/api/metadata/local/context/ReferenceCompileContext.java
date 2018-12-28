package net.n2oapp.framework.api.metadata.local.context;

import net.n2oapp.framework.api.metadata.SourceMetadata;

/**
 * Контекст для ссылок на смежные метаданные.
 * Этот контекст используется для организации связности метаданных (для использования в addDependent).
 * С этим контекстом не компилируются и не трансформируются метаданные.
 * Этот контекст не сохраняется в регистре и имеет metadataId и metadataClass из parentContext.
 */
public class ReferenceCompileContext<T extends SourceMetadata, P extends CompileContext> extends BaseCompileContext<T,P> {

    public ReferenceCompileContext(P initiator, String id, Class<T> metadataClass ) {
        super(initiator, id, metadataClass);
    }

    @Override
    public String getIdByContext() {
        return getParentContextId() + "ref_" + getMetadataId();
    }

    @Override
    public Class<T> getMetadataClassByContext() {
        throw new UnsupportedOperationException("use getMetadataClass");
    }

}
