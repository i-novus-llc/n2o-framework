package net.n2oapp.framework.config.metadata.compile.toolbar;

import net.n2oapp.framework.api.StringUtils;
import net.n2oapp.framework.api.exception.N2oException;
import net.n2oapp.framework.api.metadata.aware.DatasourceIdAware;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.compile.building.Placeholders;
import net.n2oapp.framework.api.metadata.control.N2oButtonField;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.Button;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.Confirm;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.ConfirmType;
import net.n2oapp.framework.api.metadata.local.CompiledObject;
import net.n2oapp.framework.api.metadata.meta.ModelLink;
import net.n2oapp.framework.config.metadata.compile.widget.WidgetScope;
import org.apache.commons.lang3.ArrayUtils;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Objects.nonNull;
import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.js;
import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;
import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.ref;
import static net.n2oapp.framework.config.util.DatasourceUtil.getClientDatasourceId;
import static org.apache.commons.lang3.ArrayUtils.isEmpty;

public class ButtonCompileUtil {

    public static Confirm compileConfirm(Button source, CompileProcessor p, CompiledObject.Operation operation) {
        boolean operationConfirm = nonNull(operation) && nonNull(operation.getConfirm()) && operation.getConfirm();
        if (nonNull(source.getConfirm())) {
            Object condition = p.resolveJS(source.getConfirm(), Boolean.class);
            if (condition instanceof Boolean) {
                if (!((Boolean) condition || operationConfirm))
                    return null;
                return initConfirm(source, p, operation, true);
            }
            if (condition instanceof String) {
                return initConfirm(source, p, operation, condition);
            }
        }
        if (operationConfirm)
            return initConfirm(source, p, operation, true);
        return null;
    }

    public static Confirm initConfirm(Button source, CompileProcessor p, CompiledObject.Operation operation, Object condition) {
        Confirm confirm = new Confirm();
        confirm.setMode(p.cast(source.getConfirmType(), ConfirmType.MODAL));
        confirm.setTitle(p.cast(source.getConfirmTitle(), operation != null ? operation.getFormSubmitLabel() : null, p.getMessage("n2o.confirm.title")));
        confirm.setOk(new Confirm.Button(
                p.cast(source.getConfirmOkLabel(), p.getMessage("n2o.confirm.default.okLabel")),
                p.cast(source.getConfirmOkColor(), p.resolve(property("n2o.api.button.confirm.ok_color"), String.class))));
        confirm.setCancel(new Confirm.Button(
                p.cast(source.getConfirmCancelLabel(), p.getMessage("n2o.confirm.default.cancelLabel")),
                p.cast(source.getConfirmCancelColor(), p.resolve(property("n2o.api.button.confirm.cancel_color"), String.class))));
        confirm.setText(initExpression(
                p.cast(source.getConfirmText(), operation != null ? operation.getConfirmationText() : null, p.getMessage("n2o.confirm.text"))));
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
            String clientDatasource = initClientDatasourceId(source, p);
            confirm.setModelLink(new ModelLink(source.getModel(), clientDatasource).getBindLink());
        }

        return confirm;
    }

    protected static String initClientDatasourceId(Button source, CompileProcessor p) {
        String datasourceId = initDatasource(source, p);
        if (nonNull(datasourceId))
            return getClientDatasourceId(datasourceId, p);
        else
            throw new N2oException(String.format("Need to specify 'datasource' for button with 'label=\"%s\"'", source.getLabel()));
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

    public static Boolean initValidate(Button source, CompileProcessor p, String datasource) {
        if (isEmpty(source.getActions()))
            return p.cast(source.getValidate(), false);
        return p.cast(source.getValidate(), nonNull(datasource) || nonNull(source.getValidateDatasourceIds()));
    }
}
