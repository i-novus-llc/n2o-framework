package net.n2oapp.framework.api.metadata.application;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.SourceMetadata;
import net.n2oapp.framework.api.metadata.global.N2oMetadata;

/**
 * Исходная модель подвала приложения
 */
@Getter
@Setter
public class N2oFooter extends N2oMetadata {

    /**
     * Реализация подвала приложения
     */
    private String src;

    /**
     * Текст справа
     */
    private String rightText;

    /**
     * Текст слева
     */
    private String leftText;

    /**
     * Видимость подвала приложения
     */
    private boolean visible;

    @Override
    public String getPostfix() {
        return "footer";
    }

    @Override
    public Class<? extends SourceMetadata> getSourceBaseClass() {
        return N2oFooter.class;
    }
}
