package net.n2oapp.framework.access.integration.metadata.transform;

import net.n2oapp.framework.access.metadata.schema.AccessContext;
import net.n2oapp.framework.access.metadata.schema.simple.SimpleCompiledAccessSchema;
import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.aware.PropertiesAware;
import net.n2oapp.framework.api.metadata.compile.BindProcessor;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.compile.building.Placeholders;
import net.n2oapp.framework.api.metadata.meta.page.StandardPage;
import net.n2oapp.framework.api.metadata.meta.region.CompiledRegionItem;
import net.n2oapp.framework.api.metadata.meta.region.Region;
import net.n2oapp.framework.api.metadata.meta.region.TabsRegion;
import org.springframework.stereotype.Component;

import java.util.List;

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
        if (compiled.getRegions() != null)
            for (List<Region> regions : compiled.getRegions().values())
                transform(regions);
        collectPageAccess(compiled, context.getSourceId((BindProcessor) p), accessSchema, p);
        return compiled;
    }

    /**
     * Глубинная трансформация метаданных, в ходе которой происходит слияние
     * атрибутов доступа потомков (регионов, виджетов) в регион-родитель
     *
     * @param compiledList Список метаданных
     */
    private void transform(List<? extends CompiledRegionItem> compiledList) {
        for (CompiledRegionItem compiled : compiledList) {
            if (compiled instanceof TabsRegion)
                for (TabsRegion.Tab item : ((TabsRegion) compiled).getItems()) {
                    if (item.getContent() != null) {
                        transform(item.getContent());
                        merge(item, ((List<PropertiesAware>) (List<?>) item.getContent()));
                    }
                }
            else if (compiled instanceof Region && ((Region) compiled).getContent() != null) {
                Region region = (Region) compiled;
                transform(region.getContent());
                merge(region, ((List<PropertiesAware>) (List<?>) region.getContent()));
            }
        }
    }
}

