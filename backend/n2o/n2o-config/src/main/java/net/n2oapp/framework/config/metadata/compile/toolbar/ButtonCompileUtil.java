package net.n2oapp.framework.config.metadata.compile.toolbar;

import net.n2oapp.framework.api.N2oNamespace;
import net.n2oapp.framework.api.StringUtils;
import net.n2oapp.framework.api.metadata.aware.DatasourceIdAware;
import net.n2oapp.framework.api.metadata.aware.ExtensionAttributesAware;
import net.n2oapp.framework.api.metadata.aware.GenerateAware;
import net.n2oapp.framework.api.metadata.compile.ButtonGeneratorFactory;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.compile.building.Placeholders;
import net.n2oapp.framework.api.metadata.control.N2oButtonField;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.*;
import net.n2oapp.framework.api.metadata.local.CompiledObject;
import net.n2oapp.framework.api.metadata.meta.ModelLink;
import net.n2oapp.framework.api.metadata.meta.widget.toolbar.AbstractButton;
import net.n2oapp.framework.config.metadata.compile.widget.WidgetScope;
import org.apache.commons.lang3.ArrayUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Objects.nonNull;
import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.*;
import static net.n2oapp.framework.api.metadata.local.util.CompileUtil.castDefault;
import static net.n2oapp.framework.config.util.DatasourceUtil.getClientDatasourceId;
import static org.apache.commons.lang3.ArrayUtils.isEmpty;

public class ButtonCompileUtil {

    public static Confirm compileConfirm(Button source, CompileProcessor p, CompiledObject.Operation operation) {
        if (nonNull(source.getConfirm())) {
            Object condition = p.resolveJS(source.getConfirm(), Boolean.class);
            if (condition instanceof Boolean) {
                if (!((Boolean) condition))
                    return null;
                return initConfirm(source, p, operation, true);
            }
            if (condition instanceof String) {
                return initConfirm(source, p, operation, condition);
            }
        }
        return null;
    }

    public static Confirm initConfirm(Button source, CompileProcessor p, CompiledObject.Operation operation, Object condition) {
        Confirm confirm = new Confirm();
        confirm.setMode(castDefault(source.getConfirmType(), ConfirmType.MODAL));
        confirm.setTitle(castDefault(source.getConfirmTitle(), () -> p.getMessage("n2o.confirm.title")));
        confirm.setOk(new Confirm.Button(
                castDefault(source.getConfirmOkLabel(), () -> p.getMessage("n2o.confirm.default.okLabel")),
                castDefault(source.getConfirmOkColor(), () -> p.resolve(property("n2o.api.button.confirm.ok_color"), String.class))));
        confirm.setCancel(new Confirm.Button(
                castDefault(source.getConfirmCancelLabel(), () -> p.getMessage("n2o.confirm.default.cancelLabel")),
                castDefault(source.getConfirmCancelColor(), () -> p.resolve(property("n2o.api.button.confirm.cancel_color"), String.class))));
        confirm.setText(initExpression(castDefault(source.getConfirmText(), () -> p.getMessage("n2o.confirm.text"))));
        confirm.setCondition(initConfirmCondition(condition));
        confirm.setCloseButton(p.resolve(property("n2o.api.button.confirm.close_button"), Boolean.class));
        confirm.setReverseButtons(p.resolve(property("n2o.api.button.confirm.reverse_buttons"), Boolean.class));

        if (source instanceof N2oButtonField && StringUtils.hasLink(confirm.getText())) {
            Set<String> links = StringUtils.collectLinks(confirm.getText());
            String text = Placeholders.js("'" + confirm.getText() + "'");
            for (String link : links) {
                text = text.replace(Placeholders.ref(link), "' + this." + link + " + '");
            }
            confirm.setText(text);
        }

        if (StringUtils.isJs(confirm.getText()) || StringUtils.isJs(confirm.getCondition())) {
            String clientDatasource = getClientDatasourceId(initDatasource(source, p), p);
            confirm.setModelLink(new ModelLink(source.getModel(), clientDatasource).getBindLink());
        }

        return confirm;
    }

    public static String initDatasource(DatasourceIdAware source, CompileProcessor p) {
        if (nonNull(source.getDatasourceId()))
            return source.getDatasourceId();
        WidgetScope widgetScope = p.getScope(WidgetScope.class);
        if (nonNull(widgetScope))
            return widgetScope.getDatasourceId();
        return null;
    }

    public static String initConfirmCondition(Object condition) {
        if (condition instanceof Boolean)
            return Placeholders.js(Boolean.toString(true));
        return initExpression((String) condition);
    }

    public static String initExpression(String attr) {
        if (StringUtils.hasLink(attr)) {
            Set<String> links = StringUtils.collectLinks(attr);
            String text = js("'" + attr + "'");
            for (String link : links) {
                text = text.replace(ref(link), "' + this." + link + " + '");
            }
            return text;
        }
        return attr;
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
