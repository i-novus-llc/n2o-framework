package net.n2oapp.framework.config.metadata.compile.control;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.control.plain.N2oRating;
import net.n2oapp.framework.api.metadata.meta.control.Rating;
import net.n2oapp.framework.api.metadata.meta.control.StandardField;
import org.springframework.stereotype.Component;

import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;

/**
 * Компиляция поля для ввода и отображения рейтинга
 */
@Component
public class RatingCompiler extends StandardFieldCompiler<Rating, N2oRating> {
    @Override
    protected String getControlSrcProperty() {
        return "n2o.api.control.rating.src";
    }

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oRating.class;
    }

    @Override
    public StandardField<Rating> compile(N2oRating source, CompileContext<?, ?> context, CompileProcessor p) {
        Rating rating = new Rating();
        rating.setMax(p.cast(source.getMax(),
                p.resolve(property("n2o.api.control.rating.max"), Integer.class)));
        rating.setHalf(p.cast(source.getHalf(),
                p.resolve(property("n2o.api.control.rating.half"), Boolean.class)));
        rating.setShowTooltip(source.getShowTooltip());

        return compileStandardField(rating, source, context, p);
    }

}
