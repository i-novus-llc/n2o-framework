package net.n2oapp.framework.config.metadata.compile.action;

import net.n2oapp.framework.api.StringUtils;
import net.n2oapp.framework.api.exception.N2oException;
import net.n2oapp.framework.api.metadata.ReduxModelEnum;
import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.action.N2oAlertAction;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.meta.ModelLink;
import net.n2oapp.framework.api.metadata.meta.action.alert.AlertAction;
import net.n2oapp.framework.api.metadata.meta.action.alert.AlertActionPayload;
import net.n2oapp.framework.api.metadata.meta.widget.MessagePlacementEnum;
import net.n2oapp.framework.api.ui.ResponseMessage;
import net.n2oapp.framework.config.register.route.RouteUtil;
import net.n2oapp.framework.config.util.StylesResolver;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.Collections;
import java.util.List;

import static net.n2oapp.framework.api.StringUtils.isJs;
import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;
import static net.n2oapp.framework.api.metadata.local.util.CompileUtil.castDefault;
import static net.n2oapp.framework.config.metadata.compile.action.ActionCompileStaticProcessor.*;
import static net.n2oapp.framework.config.util.DatasourceUtil.getClientDatasourceId;

/**
 * Сборка действия уведомления
 */
@Component
public class AlertActionCompiler extends AbstractActionCompiler<AlertAction, N2oAlertAction> {
    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oAlertAction.class;
    }

    @Override
    public AlertAction compile(N2oAlertAction source, CompileContext<?, ?> context, CompileProcessor p) {
        initDefaults(source, p);
        AlertAction alertAction = new AlertAction();
        alertAction.setType(p.resolve(property("n2o.api.action.alert.type"), String.class));
        compileAction(alertAction, source, p);
        alertAction.setPayload(initPayload(source, p));
        return alertAction;
    }

    private AlertActionPayload initPayload(N2oAlertAction source, CompileProcessor p) {
        AlertActionPayload payload = new AlertActionPayload();
        payload.setKey(castDefault(p.resolve(source.getPlacement(), MessagePlacementEnum.class),
                () -> p.resolve(property("n2o.api.action.alert.placement"), MessagePlacementEnum.class)));
        payload.setAlerts(initMessage(source, p));
        return payload;
    }

    private List<ResponseMessage> initMessage(N2oAlertAction source, CompileProcessor p) {
        ResponseMessage message = new ResponseMessage();
        message.setTitle(p.resolveJS(source.getTitle()));
        if (source.getText() != null)
            message.setText(p.resolveJS(source.getText().trim()));
        message.setStyle(StylesResolver.resolveStyles(source.getStyle()));
        message.setClassName(source.getCssClass());
        message.setHref(StringUtils.hasLink(source.getHref())
                ? p.resolveJS(source.getHref())
                : RouteUtil.normalize(source.getHref()));
        message.setSeverity(castDefault(source.getColor(),
                () -> p.resolve(property("n2o.api.action.alert.color"), String.class)));
        message.setCloseButton(castDefault(source.getCloseButton(),
                () -> p.resolve(property("n2o.api.action.alert.close_button"), Boolean.class)));
        message.setPlacement(castDefault(p.resolve(source.getPlacement(), MessagePlacementEnum.class),
                () -> p.resolve(property("n2o.api.action.alert.placement"), MessagePlacementEnum.class)));
        message.setTimeout(castDefault(p.resolve(source.getTimeout(), Integer.class),
                () -> p.resolve(property(String.format("n2o.api.message.%s.timeout", message.getSeverity())), Integer.class)));
        message.setTime(initTimeStamp(source));

        if (isJs(message.getText()) || isJs(message.getTitle()) || isJs(message.getHref())) {
            String datasourceId = castDefault(source.getDatasourceId(), () -> getLocalDatasourceId(p));
            ReduxModelEnum reduxModel = castDefault(source.getModel(), () -> getLocalModel(p));
            if (datasourceId == null) {
                throw new N2oException("Источник данных не найден для действия \"<alert>\" со связанными атрибутами");
            }
            message.setModelLink(new ModelLink(reduxModel, getClientDatasourceId(datasourceId, p)).getLink());
        }

        return Collections.singletonList(message);
    }

    private LocalDateTime initTimeStamp(N2oAlertAction source) {
        if (source.getTime() == null)
            return null;
        try {
            return LocalDateTime.parse(source.getTime());
        } catch (DateTimeParseException e) {
            throw new N2oException("Формат даты и времени, используемый в атрибуте 'time', не соответствует ISO_LOCAL_DATE_TIME");
        }
    }

}
