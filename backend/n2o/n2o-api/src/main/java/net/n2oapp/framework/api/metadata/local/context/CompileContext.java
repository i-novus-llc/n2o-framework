package net.n2oapp.framework.api.metadata.local.context;

import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.SourceMetadata;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.global.N2oMetadata;
import net.n2oapp.framework.api.metadata.global.aware.IdAware;
import net.n2oapp.framework.api.metadata.local.GlobalMetadataProvider;
import org.springframework.util.SerializationUtils;

import java.io.Serializable;

/**
 * Контекст расположения метаданной.
 * Создаётся при компиляции, для регистрации и определения местонахождения метаданной.
 *
 * Метаданные могут быть вложены в другие метаданные (inner), и могут быть внешние (outer) и находиться в файлах.
 * Контекст идентифицирует точное место где расположены внутренние метаданные.
 */
@Deprecated //use net.n2oapp.framework.api.metadata.compile.CompileContext
public interface CompileContext<T extends SourceMetadata, P extends CompileContext> extends
        net.n2oapp.framework.api.metadata.compile.CompileContext<Compiled, T>, IdAware, Serializable {

    /**
     * Идентификатор контекста.
     *
     * Формат: [parentContextId].[prefix]_[id]
     * parentContextId идентификатор родительского контекста
     * prefix какой-либо префикс связанный с типом расположения (не обязателен)
     * id какой-либо идентификатор места (например, id кнопки menu-item для page, открываемого по кнопке)
     */
    //todo избавиться от id, теперь вместо него должен быть url. у getUrl убрать деволтную реализацию
    @Deprecated
    @Override
    String getId();

    /**
     * URL по которому можно получить метаданную.
     * По сути является идентификатором контекста
     * @return
     */
    default String getUrl() {
        return "";
    }

    /**
     * Задание URL по которому можно получить метаданную.
     */
    default void setUrl(String url) {
    }

    /**
     * Ссылка на внешнюю метаданную.
     * Может быть null, когда метаданная задана внутри другой (например, widget внутри контейнера page)
     */
    String getMetadataId();

    /**
     * Тип метаданной
     */
    Class<T> getMetadataClass();

    /**
     * Получить метаданную.
     * @param tokens динамическая часть в идентификаторе метаданной
     * Например, "page.main.create$gender", где "gender" - это token
     * Может быть несколько token через запятую "page.main.create$gender,man"
     * @param provider провайдер глобальных метаданных
     * @return метаданная
     */
    T getMetadata(GlobalMetadataProvider provider, String... tokens);

    /**
     * Идентификатор родительского контекста
     */
    String getParentContextId();

    /**
     * Тип родителькой метаданной
     */
    Class<? extends N2oMetadata> getParentContextClass();

    /**
     * Получить родительский контекст
     * @param provider провайдер глобальных метаданных
     * @return родительский контекст
     */
    P getParentContext(GlobalMetadataProvider provider);

    /**
     * Уровень вложенности
     */
    int getNesting();

    /**
     * Установить признак вложенной метаданной
     */
    void setInner(boolean inner);

    /**
     * Вложенная метаданная?
     */
    boolean isInner();

    /**
     * Очистить все кэши в контексте после завершения компиляции
     */
    void clearCache();

    /**
     * Динамический ли контекст? Когда невозможно получить метаданную, т.к. в ней есть не разрешенные ссылки.
     */
    boolean isDynamicContext();

    /**
     * Вставить ключи для разрешения ссылок в идентификаторе метаданной
     */
    void setTokens(String... tokens);

    /**
     * Получить ключи для разрешения ссылок
     */
    String[] getTokens();

    /**
     * Получить копию контекста с разрешенными ссылками в метаданной
     * @param tokens ключи для разрешения ссылок
     */
    @SuppressWarnings("unchecked")
    default CompileContext<T,P> resolveDynamicContext(String... tokens) {
        CompileContext copy = (CompileContext) SerializationUtils.deserialize(SerializationUtils.serialize(this));
        copy.setTokens(tokens);
        return copy;
    }

    @Override
    default String getSourceId(CompileProcessor p) {
        return getMetadataId();
    }

    @Override
    default String getRoute(CompileProcessor p) {
        return null;
    }

    @Override
    default String getCompiledId(CompileProcessor p) {
        return getId();
    }

    @Override
    default Class<T> getSourceClass() {
        return getMetadataClass();
    }

    @Override
    default Class<Compiled> getCompiledClass() {
        return null;
    }
}
