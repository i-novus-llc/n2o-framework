package net.n2oapp.framework.api.metadata.local;

import net.n2oapp.framework.api.metadata.SourceMetadata;
import net.n2oapp.framework.api.metadata.compile.SourceMerger;
import net.n2oapp.framework.api.metadata.local.util.CompileUtil;

import java.io.Serializable;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * @author V. Alexeev.
 * Реализация этого класса позволяет переопределить данные в модели переданной по ref-id
 * Например: <fs:field-set label="Внешний филдсет с внутренним заголовком" ref-id="testFieldset"/>
 * {@link net.n2oapp.framework.api.metadata.local.merger.N2oFieldSetMerger} переопределит label в testFieldset
 */
@Deprecated //use SourceMerger
public abstract class N2oMetadataMerger<C extends SourceMetadata> implements SourceMerger<C>, Serializable {

    protected N2oMetadataMerger() {}

    @Override
    public final C merge(C source, C override) {
        C copy = CompileUtil.copy(source);
        mergeOverrideToSource(copy, override);
        return copy;
    }

    /**
     * Сливание метаданных, переопределять необходимо source
     * @param source модель переданная по ref-id
     * @param override свойства переопределённые в xml
     */
    protected abstract void mergeOverrideToSource(C source, C override);
}
