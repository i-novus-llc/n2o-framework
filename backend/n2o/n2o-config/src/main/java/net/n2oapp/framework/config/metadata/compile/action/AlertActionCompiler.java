package net.n2oapp.framework.config.metadata.compile.action;

import net.n2oapp.framework.api.exception.N2oException;
import net.n2oapp.framework.api.metadata.ReduxModel;
import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.meta.ModelLink;
import net.n2oapp.framework.api.metadata.action.N2oAlertAction;
import net.n2oapp.framework.api.metadata.meta.action.alert.AlertAction;
import net.n2oapp.framework.api.metadata.meta.action.alert.AlertActionPayload;
import net.n2oapp.framework.api.metadata.meta.widget.MessagePlacement;
import net.n2oapp.framework.api.ui.ResponseMessage;
import net.n2oapp.framework.config.util.StylesResolver;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.Collections;
import java.util.List;

import static net.n2oapp.framework.api.StringUtils.isJs;
import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;
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
        initDefaults(source, context, p);
        AlertAction alertAction = new AlertAction();
        alertAction.setType(p.resolve(property("n2o.api.action.alert.type"), String.class));
        compileAction(alertAction, source, p);
        alertAction.setPayload(initPayload(source, p));
        return alertAction;
    }

    private AlertActionPayload initPayload(N2oAlertAction source, CompileProcessor p) {
        AlertActionPayload payload = new AlertActionPayload();
        payload.setKey(p.cast(p.resolve(source.getPlacement(), MessagePlacement.class),
                p.resolve(property("n2o.api.action.alert.placement"), MessagePlacement.class)));
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
        message.setHref(p.resolveJS(source.getHref()));
        message.setSeverity(p.cast(source.getColor(),
                p.resolve(property("n2o.api.action.alert.color"), String.class)));
        message.setCloseButton(p.cast(source.getCloseButton(),
                p.resolve(property("n2o.api.action.alert.close_button"), Boolean.class)));
        message.setPlacement(p.cast(p.resolve(source.getPlacement(), MessagePlacement.class),
                p.resolve(property("n2o.api.action.alert.placement"), MessagePlacement.class)));//fixme добавить резолв из контекста
        message.setTimeout(p.cast(p.resolve(source.getTimeout(), Integer.class),
                p.resolve(property(String.format("n2o.api.message.%s.timeout", message.getSeverity())), Integer.class)));
        message.setTime(initTimeStamp(source));

        if (isJs(message.getText()) || isJs(message.getTitle()) || isJs(message.getHref())) {
            String datasourceId = p.cast(source.getDatasourceId(), getLocalDatasourceId(p));
            ReduxModel reduxModel = p.cast(source.getModel(), getLocalModel(p));
            if (datasourceId == null) {
                throw new N2oException("Datasource not found for action <alert> with linked attributes");
            }
            message.setModelLink(new ModelLink(reduxModel, getClientDatasourceId(datasourceId, p)).getBindLink());
        }

        return Collections.singletonList(message);
    }

    private LocalDateTime initTimeStamp(N2oAlertAction source) {
        if (source.getTime() == null)
            return null;
        try {
            return LocalDateTime.parse(source.getTime());
        } catch (DateTimeParseException e) {
            throw new N2oException("Формат даты и времени, используемый в атрибуте time, не соответствует ISO_LOCAL_DATE_TIME");
        }
    }

}
