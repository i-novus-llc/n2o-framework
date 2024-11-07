package net.n2oapp.framework.config.metadata.compile.control;

import net.n2oapp.framework.api.exception.N2oException;
import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.control.interval.N2oDateInterval;
import net.n2oapp.framework.api.metadata.domain.Domain;
import net.n2oapp.framework.api.metadata.meta.ModelLink;
import net.n2oapp.framework.api.metadata.meta.ReduxAction;
import net.n2oapp.framework.api.metadata.meta.control.DateInterval;
import net.n2oapp.framework.api.metadata.meta.control.DefaultValues;
import net.n2oapp.framework.api.metadata.meta.control.StandardField;
import net.n2oapp.framework.api.metadata.meta.widget.WidgetParamScope;
import net.n2oapp.framework.config.metadata.compile.redux.Redux;
import net.n2oapp.framework.config.metadata.compile.widget.WidgetScope;
import org.springframework.stereotype.Component;

import java.util.HashMap;

import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.colon;
import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;
import static net.n2oapp.framework.api.metadata.local.util.CompileUtil.castDefault;

@Component
public class DateIntervalCompiler extends StandardFieldCompiler<DateInterval, N2oDateInterval> {
    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oDateInterval.class;
    }

    @Override
    public StandardField<DateInterval> compile(N2oDateInterval source, CompileContext<?, ?> context, CompileProcessor p) {
        DateInterval dateInterval = new DateInterval();
        source.setDomain(p.resolve(property("n2o.api.control.date_interval.domain"), String.class));
        Domain domain = Domain.getByName(source.getDomain());
        if (domain == null || domain.getJsFormat() == null)
            throw new N2oException("Несуществующее значение атрибута 'domain' для поля '" + source.getId() + "'");
        dateInterval.setOutputFormat(domain.getJsFormat());
        dateInterval.setDateFormat(castDefault(source.getDateFormat(),
                () -> p.resolve(property("n2o.api.control.date_interval.date_format"), String.class)));
        dateInterval.setTimeFormat(source.getTimeFormat());
        dateInterval.setMin(p.resolveJS(source.getMin()));
        dateInterval.setMax(p.resolveJS(source.getMax()));
        dateInterval.setUtc(castDefault(source.getUtc(),
                () -> p.resolve(property("n2o.api.control.date_interval.utc"), Boolean.class)));
        return compileStandardField(dateInterval, source, context, p);
    }

    @Override
    protected Object compileDefValues(N2oDateInterval source, CompileProcessor p) {
        if (source.getBegin() == null && source.getEnd() == null)
            return null;
        DefaultValues values = new DefaultValues();
        values.setValues(new HashMap<>());
        if (source.getBegin() != null) {
            values.getValues().put("begin", source.getBegin());
        }
        if (source.getEnd() != null) {
            values.getValues().put("end", source.getEnd());
        }
        return values;
    }

    @Override
    protected void compileParams(StandardField<DateInterval> control, N2oDateInterval source, WidgetParamScope paramScope, CompileProcessor p) {
        if (source.getBeginParam() == null && source.getEndParam() == null) return;
        WidgetScope widgetScope = p.getScope(WidgetScope.class);
        if (widgetScope == null)
            return;
        if (source.getBeginParam() != null) {
            String fieldId = control.getId() + ".begin";
            ModelLink onSet = new ModelLink(widgetScope.getModel(), widgetScope.getClientDatasourceId(), fieldId);
            onSet.setParam(source.getBeginParam());
            ReduxAction onGet = Redux.dispatchUpdateModel(widgetScope.getClientDatasourceId(), widgetScope.getModel(), fieldId,
                    colon(source.getBeginParam()));
            paramScope.addQueryMapping(source.getBeginParam(), onGet, onSet);
        }
        if (source.getEndParam() != null) {
            String fieldId = control.getId() + ".end";
            ModelLink onSet = new ModelLink(widgetScope.getModel(), widgetScope.getClientDatasourceId(), fieldId);
            onSet.setParam(source.getEndParam());
            ReduxAction onGet = Redux.dispatchUpdateModel(widgetScope.getClientDatasourceId(), widgetScope.getModel(), fieldId,
                    colon(source.getEndParam()));
            paramScope.addQueryMapping(source.getEndParam(), onGet, onSet);
        }
    }

    @Override
    protected String getControlSrcProperty() {
        return "n2o.api.control.date_interval.src";
    }
}
