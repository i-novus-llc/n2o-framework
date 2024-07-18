package net.n2oapp.framework.access.integration.metadata.transform;

import net.n2oapp.framework.access.metadata.schema.AccessContext;
import net.n2oapp.framework.access.metadata.schema.simple.SimpleCompiledAccessSchema;
import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.compile.BindProcessor;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.compile.building.Placeholders;
import net.n2oapp.framework.api.metadata.meta.page.StandardPage;
import org.springframework.stereotype.Component;

/**
 * Трансформатор доступа страницы
 */
@Component
public class PageAccessTransformer extends BaseAccessTransformer<StandardPage, CompileContext<?, ?>> {

    @Override
    public Class<? extends Compiled> getCompiledClass() {
        return StandardPage.class;
    }

    /**
     * Трансформация доступа собранных метаданных стандартной страницы
     *
     * @param compiled Метаданные стандартной страницы
     * @param context  Контекст сборки метаданных
     * @param p        Процессор сборки метаданных
     * @return Трансформированные метаданные стандартной страницы
     */
    @Override
    public StandardPage transform(StandardPage compiled, CompileContext context, CompileProcessor p) {
        SimpleCompiledAccessSchema accessSchema = (SimpleCompiledAccessSchema)
                p.getCompiled(new AccessContext(p.resolve(Placeholders.property("n2o.access.schema.id"), String.class)));
        collectPageAccess(compiled, context.getSourceId((BindProcessor) p), accessSchema, p);
        return compiled;
    }
}

