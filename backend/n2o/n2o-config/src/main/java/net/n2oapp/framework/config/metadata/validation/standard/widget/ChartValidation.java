package net.n2oapp.framework.config.metadata.validation.standard.widget;

import net.n2oapp.framework.api.metadata.global.dao.N2oQuery;
import net.n2oapp.framework.api.metadata.global.view.widget.N2oChart;
import net.n2oapp.framework.api.metadata.global.view.widget.chart.Display;
import net.n2oapp.framework.api.metadata.global.view.widget.chart.N2oChartValue;
import net.n2oapp.framework.api.metadata.validate.ValidateProcessor;
import net.n2oapp.framework.api.metadata.validation.TypedMetadataValidator;
import net.n2oapp.framework.api.metadata.validation.exception.N2oMetadataValidationException;
import net.n2oapp.framework.config.reader.ReferentialIntegrityViolationException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Валидация графиков
 */
@Component
public class ChartValidation extends TypedMetadataValidator<N2oChart> {

    @Override
    public Class<N2oChart> getSourceClass() {
        return N2oChart.class;
    }

    @Override
    public void validate(N2oChart metadata, ValidateProcessor p) {
        Display display = metadata.getDisplay();
        N2oQuery query = p.getOrNull(metadata.getQueryId(), N2oQuery.class);
        if(query == null) throw new ReferentialIntegrityViolationException(metadata.getQueryId(), metadata.getClass());
        List<N2oQuery.Field> fields = Arrays.asList(query.getFields());

        if (display == null) {
            throw new N2oMetadataValidationException("n2o.displayTagNotFilled");
        }

        boolean labelExist = existField(metadata.getLabelFieldId(), fields);
        if(!labelExist) {
            throw new N2oMetadataValidationException("n2o.labelFieldNotExists");
        }

        if (display.getType().equals(Display.Type.LINE)) {
            if (metadata.getValues() == null) throw new N2oMetadataValidationException("n2o.valuesNotExists");
            List<String> fieldsNameNotExists = new ArrayList<>();
            for (N2oChartValue value : metadata.getValues()) {
                if(!existField(value.getFieldId(), fields)) {
                    fieldsNameNotExists.add(value.getFieldId());
                }
            }

            if(!fieldsNameNotExists.isEmpty()) {
                throw new N2oMetadataValidationException("n2o.fieldsNotExists").addData(fieldsNameNotExists.stream().collect(Collectors.joining(" , ")));
            }
        } else {
            if (metadata.getValueFieldId() == null) throw new N2oMetadataValidationException("n2o.valueFieldNotExistsShort");
            boolean valueExist = existField(metadata.getValueFieldId(), fields);
            if(!valueExist) throw new N2oMetadataValidationException("n2o.valueFieldNotExists");
        }

    }

    private boolean existField(String fieldId, List<N2oQuery.Field> fields) {
        return fields.stream().anyMatch(f -> f.getId().equals(fieldId));
    }

}
