package net.n2oapp.framework.config.metadata.compile.widget;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.global.dao.validation.N2oValidation;


/**
 * Информация о моменте валидирования.
 */
@Getter
@Setter
public class MomentScope {
    private N2oValidation.ServerMoment moment;

    public MomentScope(N2oValidation.ServerMoment moment) {
        this.moment = moment;
    }
}
