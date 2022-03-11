package net.n2oapp.framework.config.metadata.compile.widget;

import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.compile.BindProcessor;
import net.n2oapp.framework.api.metadata.meta.widget.Cards;
import org.springframework.stereotype.Component;

/**
 * Связывание данных в виджете Карточки
 */
@Component
public class CardsBinder extends BaseListWidgetBinder<Cards> {
    @Override
    public Class<? extends Compiled> getCompiledClass() {
        return Cards.class;
    }

    @Override
    public Cards bind(Cards compiled, BindProcessor p) {
        if (compiled.getCards() != null)
            compiled.getCards().stream()
                    .flatMap(x -> x.getContent().stream().map(Cards.Block::getComponent)).forEach(p::bind);
        return compiled;
    }
}
