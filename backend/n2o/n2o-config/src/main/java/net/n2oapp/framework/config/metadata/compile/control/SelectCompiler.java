package net.n2oapp.framework.config.metadata.compile.control;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.control.list.ListTypeEnum;
import net.n2oapp.framework.api.metadata.control.list.N2oSelect;
import net.n2oapp.framework.api.metadata.meta.control.Select;
import net.n2oapp.framework.api.metadata.meta.control.StandardField;
import org.springframework.stereotype.Component;

import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;
import static net.n2oapp.framework.api.metadata.local.util.CompileUtil.castDefault;


/**
 * Компиляция компонента выбора из выпадающего списка
 */
@Component
public class SelectCompiler extends ListControlCompiler<Select, N2oSelect> {

    @Override
    protected String getControlSrcProperty() {
        return "n2o.api.control.select.src";
    }

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oSelect.class;
    }

    @Override
    public StandardField<Select> compile(N2oSelect source, CompileContext<?, ?> context, CompileProcessor p) {
        Select control = new Select();
        control.setType(castDefault(source.getType(),
                () -> p.resolve(property("n2o.api.control.select.type"), ListTypeEnum.class)));
        control.setClosePopupOnSelect(!ListTypeEnum.CHECKBOXES.equals(control.getType()));
        control.setCleanable(castDefault(source.getCleanable(),
                () -> p.resolve(property("n2o.api.control.select.cleanable"), Boolean.class)));
        control.setSelectFormat(source.getSelectFormat());
        control.setSelectFormatOne(source.getSelectFormatOne());
        control.setSelectFormatFew(source.getSelectFormatFew());
        control.setSelectFormatMany(source.getSelectFormatMany());
        control.setDescriptionFieldId(source.getDescriptionFieldId());
        return compileListControl(control, source, context, p);
    }
}
