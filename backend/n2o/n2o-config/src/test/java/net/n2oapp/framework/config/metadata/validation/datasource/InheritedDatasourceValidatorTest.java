package net.n2oapp.framework.config.metadata.validation.datasource;

import net.n2oapp.framework.api.metadata.validation.exception.N2oMetadataValidationException;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.pack.N2oAllDataPack;
import net.n2oapp.framework.config.metadata.pack.N2oPagesPack;
import net.n2oapp.framework.config.metadata.pack.N2oRegionsPack;
import net.n2oapp.framework.config.metadata.pack.N2oWidgetsPack;
import net.n2oapp.framework.config.metadata.validation.standard.datasource.InheritedDatasourceValidator;
import net.n2oapp.framework.config.metadata.validation.standard.page.BasePageValidator;
import net.n2oapp.framework.config.metadata.validation.standard.page.PageValidator;
import net.n2oapp.framework.config.metadata.validation.standard.widget.WidgetValidator;
import net.n2oapp.framework.config.test.SourceValidationTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Тестирование валидации источника данных, получающего данные из другого источника данных
 */
public class InheritedDatasourceValidatorTest extends SourceValidationTestBase {

    @Override
    @BeforeEach
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oPagesPack(), new N2oRegionsPack(), new N2oWidgetsPack(), new N2oAllDataPack());
        builder.validators(new PageValidator(), new WidgetValidator(), new BasePageValidator(), new InheritedDatasourceValidator());
    }

    /**
     * Проверяется наличие атрибута source-datasource
     */
    @Test
    void testSourceDatasourceLink() {
        assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/datasource/testNonExistSourceDatasource.page.xml"),
                "В источнике данных 'inh1' не задан атрибут 'source-datasource'"
        );
    }

    /**
     * Проверяется, что source-datasource не совпадает с id
     */
    @Test
    void testComparingSourceDatasourceAndId() {
        assertThrows(
                N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/datasource/testComparingSourceDatasourceAndId.page.xml"),
                "Атрибут 'source-datasource' источника данных 'ds1' совпадает с 'id'"
        );
    }
}
