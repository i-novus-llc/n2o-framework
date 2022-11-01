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
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * Тестирование валидации источника данных, получающего данные из другого источника данных
 */
public class InheritedDatasourceValidatorTest extends SourceValidationTestBase {

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Override
    @Before
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
    public void testSourceDatasourceLink() {
        exception.expect(N2oMetadataValidationException.class);
        exception.expectMessage("В источнике данных 'inh1' не задан атрибут 'source-datasource'");
        validate("net/n2oapp/framework/config/metadata/validation/datasource/testNonExistSourceDatasource.page.xml");
    }

    /**
     * Проверяется, что source-datasource не совпадает с id
     */
    @Test
    public void testComparingSourceDatasourceAndId() {
        exception.expect(N2oMetadataValidationException.class);
        exception.expectMessage("Атрибут 'source-datasource' источника данных 'ds1' совпадает с 'id'");
        validate("net/n2oapp/framework/config/metadata/validation/datasource/testComparingSourceDatasourceAndId.page.xml");
    }
}
