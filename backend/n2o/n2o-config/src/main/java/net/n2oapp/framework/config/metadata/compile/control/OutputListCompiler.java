package net.n2oapp.framework.config.metadata.compile.control;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.control.plain.N2oOutputList;
import net.n2oapp.framework.api.metadata.global.view.action.control.TargetEnum;
import net.n2oapp.framework.api.metadata.meta.control.OutputList;
import net.n2oapp.framework.api.metadata.meta.control.StandardField;
import org.springframework.stereotype.Component;

import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;
import static net.n2oapp.framework.api.metadata.local.util.CompileUtil.castDefault;

/**
 * Компиляция компонента вывода многострочного текста
 */
@Component
public class OutputListCompiler extends StandardFieldCompiler<OutputList, N2oOutputList> {

    @Override
    protected String getControlSrcProperty() {
        return "n2o.api.control.output_list.src";
    }

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oOutputList.class;
    }

    @Override
    public StandardField<OutputList> compile(N2oOutputList source, CompileContext<?, ?> context, CompileProcessor p) {
        OutputList outputList = new OutputList();
        outputList.setLabelFieldId(castDefault(source.getLabelFieldId(),
                () -> p.resolve(property("n2o.api.control.output_list.label_field_id"), String.class)));
        outputList.setHrefFieldId(castDefault(source.getHrefFieldId(),
                () -> p.resolve(property("n2o.api.control.output_list.href_field_id"), String.class)));
        outputList.setTarget(castDefault(source.getTarget(),
                () -> p.resolve(property("n2o.api.control.output_list.target"), TargetEnum.class)));
        outputList.setDirection(castDefault(source.getDirection(),
                () -> p.resolve(property("n2o.api.control.output_list.direction"), OutputList.DirectionEnum.class)));
        outputList.setSeparator(castDefault(source.getSeparator(),
                () -> p.resolve(property("n2o.api.control.output_list.separator"), String.class)));
        return compileStandardField(outputList, source, context, p);
    }
}
