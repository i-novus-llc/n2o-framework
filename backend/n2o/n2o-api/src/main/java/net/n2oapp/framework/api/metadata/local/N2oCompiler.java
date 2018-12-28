package net.n2oapp.framework.api.metadata.local;

import net.n2oapp.framework.api.DynamicUtil;
import net.n2oapp.framework.api.metadata.ClientMetadata;
import net.n2oapp.framework.api.metadata.CompiledMetadata;
import net.n2oapp.framework.api.metadata.MetaModel;
import net.n2oapp.framework.api.metadata.SourceMetadata;
import net.n2oapp.framework.api.metadata.event.factory.EventCompilerFactory;
import net.n2oapp.framework.api.metadata.global.view.page.N2oPage;
import net.n2oapp.framework.api.metadata.local.context.CompileContext;
import net.n2oapp.framework.api.metadata.local.context.ContextMetadataProvider;
import net.n2oapp.framework.api.metadata.local.util.CompileUtil;
import net.n2oapp.framework.api.metadata.local.util.MetadataHolder;
import net.n2oapp.framework.api.script.ScriptProcessor;
import net.n2oapp.framework.api.user.StaticUserContext;
import net.n2oapp.framework.api.user.UserContext;
import net.n2oapp.properties.StaticProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.SerializationUtils;

import java.util.function.Supplier;

/**
 * Получение собранных метаданных
 */
@Deprecated //use CompileProcessor
public interface N2oCompiler extends GlobalMetadataProvider {

    Logger logger = LoggerFactory.getLogger(N2oCompiler.class);

    default N2oPage createSimplePage(String widgetId) {
        try {
            return CompileUtil.createSimplePage(widgetId, this);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            try {
                return CompileUtil.createSimplePage((String) getProperty("n2o.api.error.widget.id"), this);
            } catch (Exception e2) {
                e.addSuppressed(e2);
                throw e;
            }
        }
    }


    <T extends SourceMetadata> boolean isExists(String id, Class<T> metadataClass);

    <T extends CompiledMetadata> T get(String id, Class<T> compiledClass);

    ClientMetadata map(String id, Class<? extends CompiledMetadata> compiledClass, UserContext user);

    <C extends CompiledMetadata, T extends SourceMetadata, D extends CompileContext> C compile(T n2o, D context);

    <D extends CompileContext> String register(D context);

    <T extends CompiledMetadata, D extends CompileContext> T registerAndCompile(D context);

    default <T extends CompiledMetadata, D extends CompileContext> Supplier<T> registerAndGet(D context) {
        return new MetadataHolder<>(registerAndCompile(context), this);
    }


    @SuppressWarnings("unchecked")
    default <T> T copy(T cloningObject) {
        if (cloningObject == null) return null;
        return (T) SerializationUtils.deserialize(SerializationUtils.serialize(cloningObject));
    }


    @SuppressWarnings("unchecked")
    default <T extends CompiledMetadata> T get(String id, Class<T> compiledClass, CompileContext initiator) {
        return get(id, compiledClass);
    }


    public default ScriptProcessor getScriptProcessor() {
        return ScriptProcessor.getInstance();
    }

    EventCompilerFactory getEventCompilerFactory();

    default SimplePageProvider createSimplePageProvider(String formId) {
        return new SimplePageProvider(formId);
    }

    /**
     * Получение значения системной настройки
     * @param code Код настройки
     * @return Значение настройки
     */
    default Object getProperty(String code) {
        return StaticProperties.getProperty(code);
    }

    /**
     * Получение локализованной строки
     * @param code Код локализации
     * @param arguments Аргументы
     * @return Локализованная строка
     */
    String getMessage(String code, Object... arguments);
    /**
     * Получение значения контекста текущего пользователя
     * @return Значение контекста текущего пользователя
     */
    default Object getContext(String code) {
        return StaticUserContext.getUserContext().get(code);
    }

    MetaModel getMetaModelBySource(Class<? extends SourceMetadata> sourceMetadataClass);

    MetaModel getMetaModelByCompiled(Class<? extends CompiledMetadata> compiledMetadataClass);

    MetaModel getMetaModelByClient(Class<? extends ClientMetadata> clientMetadataClass);

    class SimplePageProvider<P extends CompileContext> implements ContextMetadataProvider<N2oPage, P> {
        private String formId;

        public SimplePageProvider(String formId) {
            this.formId = formId;
        }

        @Override
        public N2oPage provide(P parentContext, GlobalMetadataProvider provider) {
            return ((N2oCompiler)provider).createSimplePage(formId);
        }

        @Override
        public N2oPage provide(P parentContext, CompileContext thisContext, GlobalMetadataProvider provider) {
            return ((N2oCompiler)provider).createSimplePage(DynamicUtil.resolveTokens(formId, thisContext.getTokens()));
        }

    }

}
