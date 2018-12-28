package net.n2oapp.framework.api.metadata.local.context;

import net.n2oapp.framework.api.DynamicUtil;
import net.n2oapp.framework.api.metadata.SourceMetadata;
import net.n2oapp.framework.api.metadata.global.N2oMetadata;
import net.n2oapp.framework.api.metadata.global.N2oReference;
import net.n2oapp.framework.api.metadata.local.GlobalMetadataProvider;
import net.n2oapp.framework.api.metadata.local.N2oMetadataMerger;
import net.n2oapp.framework.api.metadata.meta.ModelLink;

import java.util.Map;

/**
 * Базовый контекст всех метаданных
 */
@Deprecated
public abstract class BaseCompileContext<T extends SourceMetadata, P extends CompileContext>
        implements CompileContext<T, P> {
    private transient T metadata;
    private transient P parentContext;
    private ContextMetadataProvider<T, P> metadataProvider;
    private N2oMetadataMerger<T> metadataMerger;
    protected String metadataId;
    protected Class<T> metadataClass;
    protected String[] tokens;
    protected String parentContextId;
    protected Class<? extends N2oMetadata> parentContextClass;

    protected int nesting;
    protected boolean real = true;

    @Override
    public Map<String, ModelLink> getQueryRouteInfos() {
        return null;
    }

    @Override
    public Map<String, ModelLink> getPathRouteInfos() {
        return null;
    }

    /**
     * Влияет ли данный контекст на компиляцию родительской метаданной
     */
    private boolean inner = false;


    protected BaseCompileContext() {
        nesting = 1;
    }

    @SuppressWarnings("unchecked")
    private BaseCompileContext(P parentContext) {
        this();
        this.parentContext = parentContext;
        if (parentContext != null) {
            this.parentContextId = parentContext.getId();
            this.parentContextClass = parentContext.getMetadataClass();
            this.nesting = parentContext.getNesting();
        }
    }

    protected BaseCompileContext(P parentContext, String id) {
        this(parentContext);
        //todo проверку на null можно будет вернуть после упразднения compileStrategy для MasterDetailContext
//        if (id == null)
//            throw new IllegalArgumentException("id can not be null" + (parentContext != null ? ", parent context [" + parentContext.getId() + "]" : ""));
        this.metadataId = id;
    }

    protected BaseCompileContext(P parentContext, String id, Class<T> metadataClass) {
        this(parentContext);
        this.metadataClass = metadataClass;
        this.metadataId = id;
    }

    @SuppressWarnings("unchecked")
    protected BaseCompileContext(P parentContext, T metadata, ContextMetadataProvider<T, P> metadataProvider) {
        this(parentContext);
        if (metadataProvider == null)
            throw new IllegalArgumentException("MetadataProvider can not be null!");
        this.metadataProvider = metadataProvider;
        this.metadata = metadata;
        if (metadata instanceof N2oReference && ((N2oReference)metadata).getRefId() != null) {
            N2oReference reference = (N2oReference) metadata;
            this.metadataId = reference.getRefId();
            this.metadataClass = (Class<T>) reference.getClass();
            this.metadataMerger = reference.getMerger();
        } else {
            this.metadataClass = metadata != null ? (Class<T>) metadata.getClass() : null;
        }
    }

    protected BaseCompileContext(P parentContext, ContextMetadataProvider<T, P> metadataProvider) {
        this(parentContext);
        if (metadataProvider == null)
            throw new IllegalArgumentException("MetadataProvider can not be null!");
        this.metadataProvider = metadataProvider;
    }

    public final String getId() {
        return DynamicUtil.reduceTokens(getIdByContext(), tokens);
    }

    /**
     * @return - оригинальный (не контекстульный) идентификатор метаданной
     * для анонимных метаданных(пр. виджет внутри пейджа) должен возвращать null!
     */
    @Deprecated //@use getMetadataId()
    public String getRealId() {
        return getMetadataId();
    }


    /**
     * Генерация идентификатора по контексту
     */
    protected abstract String getIdByContext();

    @SuppressWarnings("unchecked")
    public Class<T> getMetadataClass() {
        if (metadataClass != null)
            return metadataClass;
        return getMetadataClassByContext();
    }

    public abstract Class<T> getMetadataClassByContext();

    //геттеры
    @Override
    public int getNesting() {
        return nesting;
    }

    protected void setNesting(int nesting) {
        this.nesting = nesting;
    }

    @Deprecated
    public String getIdHash() {
        return metadataId != null ? metadataId + "." + hash(getId()) : hash(getId());
    }

    private String hash(String text) {
        return Math.abs(text.hashCode()) + "";
    }

    public P getParentContext(GlobalMetadataProvider metadataProvider) {
        if (parentContextId == null)
            return null;
        return getParentContextSync(metadataProvider);
    }

    private synchronized P getParentContextSync(GlobalMetadataProvider metadataProvider) {
        if (parentContext == null) {
            parentContext = metadataProvider.getContext(parentContextId, parentContextClass);
        }
        return parentContext;
    }

    public String getParentContextId() {
        return parentContextId;
    }

    public String generateCurrentContextId(Object... parts) {
        StringBuilder builder = new StringBuilder();
        for (Object id : parts) {
            if (id == null || id.toString().trim().isEmpty())
                continue;
            String part = id.toString().trim().replaceAll("[/.?&]", "");
            if (builder.length() > 0)
                builder.append('_');
            builder.append(part);
        }
        return getParentContextId() != null ? getParentContextId() + "." + builder.toString() : builder.toString();
    }

    public Class<? extends N2oMetadata> getParentContextClass() {
        return parentContextClass;
    }

    @Override
    public T getMetadata(GlobalMetadataProvider compiler, String... tokens) {
        T innerMetadata = null;
        T outerMetadata = null;

        if (metadata != null) {
            //вложенная метаданная в кэше
            innerMetadata = metadata;
        } else if (metadataProvider != null) {
            //вложенная метаданная вне кэша
            innerMetadata = this.metadataProvider.provide(getParentContext(compiler), this, compiler);
            if (innerMetadata == null) {
                throw new IllegalStateException("Can not resolve metadata from metadataProvider: [" + getId() + "]");
            }
        }

        if (metadataId != null) {
            //внешняя метаданная
            if (isDynamicContext() && (tokens == null || tokens.length == 0)) {
                throw new IllegalStateException("Cannot resolve references in metadataId " + metadataId);
            }
            outerMetadata = compiler.getGlobal(DynamicUtil.resolveTokens(metadataId, tokens != null && tokens.length > 0
                    ? tokens : this.tokens != null && this.tokens.length > 0
                    ? this.tokens : null), getMetadataClass());
        }

        if (innerMetadata != null && outerMetadata != null && metadataMerger != null) {
            return metadataMerger.merge(outerMetadata, innerMetadata);
        } else if (outerMetadata != null) {
            return outerMetadata;
        } else if (innerMetadata != null) {
            return innerMetadata;
        } else {
            throw new IllegalStateException("context must have metadataId or metadataProvider: [" + getId() + "]");
        }
    }

    @Override
    public void clearCache() {
        metadata = null;
        parentContext = null;
    }

    /**
     * Этот метод актуален для ValidationCompiler. Ему нужно отличать реальные метаданные от созданных движком или анонимных
     */
    public boolean containsMetadata() {
        return metadataId == null && (metadata != null || metadataProvider != null);
    }

    public String getMetadataId() {
        return DynamicUtil.resolveTokens(metadataId, tokens);
    }

    public boolean isReal() {
        return real;
    }

    protected void setReal(boolean real) {
        this.real = real;
    }

    protected void setMetadataMerger(N2oMetadataMerger<T> metadataMerger) {
        this.metadataMerger = metadataMerger;
    }

    @Override
    public boolean isInner() {
        return inner;
    }

    @Override
    public void setInner(boolean inner) {
        this.inner = inner;
    }

    @Override
    public String toString() {
        return getId();
    }

    @Override
    public boolean isDynamicContext() {
        return DynamicUtil.hasRefs(metadataId) && (this.tokens == null || this.tokens.length == 0);
    }

    @Override
    public void setTokens(String... tokens) {
        this.tokens = tokens;
    }

    @Override
    public String[] getTokens() {
        return tokens;
    }
}
