package net.n2oapp.framework.api.metadata.local;

import net.n2oapp.framework.api.metadata.CompiledMetadata;
import net.n2oapp.framework.api.metadata.SourceMetadata;
import net.n2oapp.framework.api.metadata.aware.PropertiesAware;
import net.n2oapp.framework.api.metadata.local.context.CompileContext;
import net.n2oapp.framework.api.metadata.local.util.CompileUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Базовый класс всех компилируемых метаданных
 * @param <T> Тип исходный метаданной
 * @param <D> Тип контекста компиляции
 */
@Deprecated
public abstract class AbstractCompiledMetadata<T extends SourceMetadata, D extends CompileContext>
        implements CompiledMetadata, Processable {
    protected String id;
    private transient D compileContext;
    private transient N2oCompiler compiler;
    //контракт такой. Properties всегда не null и не emptyMap!
    protected Map<String, Object> properties;
    private boolean processable = true;

    @Override
    public boolean isProcessable() {
        return processable;
    }

    public void addProperty(String key, Object value) {
        properties.put(key, value);
    }

    public Object getProperty(String key) {
        return properties.get(key);
    }


    public Map<String, Object> getProperties() {
        return properties;
    }

    public void compile(T source, N2oCompiler compiler, D context) {
        id = source.getId();
        if (source instanceof Processable)
            this.processable = CompileUtil.castDefault(((Processable)source).isProcessable(), true);
        if (source instanceof PropertiesAware)
            this.properties = ((PropertiesAware)source).getProperties() != null ? compiler.copy(((PropertiesAware)source).getProperties()) : new HashMap<>();
        this.compileContext = context;
        this.compiler = compiler;
    }

    //это свойство transient и его позволено использовать только во время компиляции
    protected D getCompileContext() {
        return compileContext;
    }

    //это свойство transient и его позволено использовать только во время компиляции
    protected N2oCompiler getCompiler() {
        return compiler != null ? compiler : CompilerHolder.get();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return getId() + "." + getClass().getSimpleName();
    }
}
