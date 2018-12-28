package net.n2oapp.framework.api.metadata.local.util;

import net.n2oapp.framework.api.metadata.global.N2oMetadata;
import net.n2oapp.framework.api.metadata.CompiledMetadata;
import net.n2oapp.framework.api.metadata.local.CompilerHolder;
import net.n2oapp.framework.api.metadata.local.N2oCompiler;
import net.n2oapp.framework.api.metadata.local.context.CompileContext;

import java.io.Serializable;
import java.util.function.Supplier;

/**
 * @author iryabov
 * @since 02.06.2016
 */
public class MetadataHolder<T extends CompiledMetadata> implements Supplier<T>, Serializable {
    private transient T compiledMetadata;
    private transient CompileContext initiator;
    private transient N2oCompiler compiler;
    private Class<T> compiledClass;
    private String id;
    private String initiatorId;
    private Class<? extends N2oMetadata> initiatorMetadataClass;

    @SuppressWarnings("unchecked")
    public MetadataHolder(String id, Class<T> compiledClass, CompileContext initiator, N2oCompiler compiler) {
        this.id = id;
        this.compiledClass = compiledClass;
        if (initiator != null) {
            this.initiator = initiator;
            this.initiatorId = initiator.getId();
            this.initiatorMetadataClass = initiator.getMetadataClass();
        }
        this.compiler = compiler;
    }

    @SuppressWarnings("unchecked")
    public MetadataHolder(T compiledMetadata, N2oCompiler compiler) {
        this.compiledMetadata = compiledMetadata;
        this.id = compiledMetadata.getId();
        this.compiledClass = (Class<T>) compiledMetadata.getClass();
        this.compiler = compiler;
    }

    private CompileContext getInitiator() {
        return initiator != null ? initiator
                : initiatorId != null ? getCompiler().getContext(initiatorId, initiatorMetadataClass)
                : null;
    }

    private N2oCompiler getCompiler() {
        return this.compiler != null ? this.compiler : CompilerHolder.get();
    }

    @SuppressWarnings("unchecked")
    @Override
    public T get() {
        T metadata = this.compiledMetadata != null ? this.compiledMetadata : getCompiler().get(id, compiledClass, getInitiator());
        this.compiledMetadata = metadata;
        return metadata;
    }
}
