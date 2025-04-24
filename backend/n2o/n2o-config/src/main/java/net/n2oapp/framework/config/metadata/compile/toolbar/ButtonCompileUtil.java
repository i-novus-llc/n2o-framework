package net.n2oapp.framework.config.metadata.compile.toolbar;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.n2oapp.framework.api.N2oNamespace;
import net.n2oapp.framework.api.metadata.aware.DatasourceIdAware;
import net.n2oapp.framework.api.metadata.aware.ExtensionAttributesAware;
import net.n2oapp.framework.api.metadata.aware.GenerateAware;
import net.n2oapp.framework.api.metadata.compile.ButtonGeneratorFactory;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.*;
import net.n2oapp.framework.api.metadata.meta.widget.toolbar.AbstractButton;
import net.n2oapp.framework.config.metadata.compile.widget.WidgetScope;
import org.apache.commons.lang3.ArrayUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Objects.nonNull;
import static net.n2oapp.framework.api.metadata.local.util.CompileUtil.castDefault;
import static net.n2oapp.framework.config.util.DatasourceUtil.getClientDatasourceId;
import static org.apache.commons.lang3.ArrayUtils.isEmpty;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ButtonCompileUtil {

    public static String initDatasource(DatasourceIdAware source, CompileProcessor p) {
        if (nonNull(source.getDatasourceId()))
            return source.getDatasourceId();
        WidgetScope widgetScope = p.getScope(WidgetScope.class);
        if (nonNull(widgetScope))
            return widgetScope.getDatasourceId();
        return null;
    }

    public static List<String> compileValidate(Button source, CompileProcessor p, String datasource) {
        if (!Boolean.TRUE.equals(source.getValidate()))
            return null;
        if (!ArrayUtils.isEmpty(source.getValidateDatasourceIds()))
            return Stream.of(source.getValidateDatasourceIds())
                    .map(ds -> getClientDatasourceId(ds, p))
                    .collect(Collectors.toList());
        if (nonNull(datasource))
            return Collections.singletonList(getClientDatasourceId(datasource, p));
        return null;
    }

    public static Boolean initValidate(Button source, String datasource) {
        if (isEmpty(source.getActions()))
            return castDefault(source.getValidate(), false);
        return castDefault(source.getValidate(), nonNull(datasource) || nonNull(source.getValidateDatasourceIds()));
    }

    public static List<AbstractButton> generateButtons(GenerateAware source, N2oToolbar toolbar, ButtonGeneratorFactory buttonGeneratorFactory,
                                                       CompileContext<?, ?> context, CompileProcessor p) {
        List<AbstractButton> generated = new ArrayList<>();
        Map<N2oNamespace, Map<String, String>> extAttributes = source instanceof N2oButton ? ((N2oButton) source).getExtAttributes() : new HashMap<>();

        List.of(source.getGenerate()).forEach(type ->
            generated.addAll(
                    buttonGeneratorFactory.generate(type, toolbar, context, p)
                            .stream()
                            .peek(item -> ((ExtensionAttributesAware) item).setExtAttributes(extAttributes))
                            .map(item -> (AbstractButton)p.compile(item, context, p))
                            .collect(Collectors.toList())
            )
        );

        return generated;
    }
}
