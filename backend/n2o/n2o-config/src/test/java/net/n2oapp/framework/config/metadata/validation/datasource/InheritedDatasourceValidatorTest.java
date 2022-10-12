package net.n2oapp.framework.config.metadata.validation.datasource;

import net.n2oapp.framework.api.metadata.validation.exception.N2oMetadataValidationException;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.pack.*;
import net.n2oapp.framework.config.metadata.validation.standard.action.PageActionValidator;
import net.n2oapp.framework.config.metadata.validation.standard.datasource.InheritedDatasourceValidator;
import net.n2oapp.framework.config.metadata.validation.standard.page.BasePageValidator;
import net.n2oapp.framework.config.metadata.validation.standard.page.PageValidator;
import net.n2oapp.framework.config.metadata.validation.standard.widget.WidgetValidator;
import net.n2oapp.framework.config.selective.CompileInfo;
import net.n2oapp.framework.config.test.SourceValidationTestBase;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

/**
 * Тестирование валидации источника данных, получающего данные из другого источника данных
 */
public class InheritedDatasourceValidatorTest extends SourceValidationTestBase {

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oPagesPack(), new N2oRegionsPack(), new N2oWidgetsPack(), new N2oAllDataPack(), new N2oActionsPack());
        builder.validators(new PageValidator(), new WidgetValidator(), new BasePageValidator(),
                new PageActionValidator(), new InheritedDatasourceValidator());
        builder.sources(new CompileInfo("net/n2oapp/framework/config/metadata/compile/stub/utBlank.page.xml"));
    }

    /**
     * Проверяется наличие datasource, на который ссылается атрибут 'source-datasource'
     */
    @Test
    public void testSourceDatasourceExistence() {
        N2oMetadataValidationException exc = assertThrows(N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/datasource/testNonExistSourceDatasource.page.xml"));
        assertEquals("Атрибут 'source-datasource' источника данных 'inh1' ссылается на несуществующий источник 'ds1'", exc.getMessage());
    }

    /**
     * Проверяется наличие datasource, на который ссылается атрибут 'source-datasource' в действии открытии страницы
     */
    @Test
    public void testSourceDatasourceInPageActionExistence() {
        N2oMetadataValidationException exc = assertThrows(N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/datasource/testNonExistSourceDatasourceInPageAction.page.xml"));
        assertEquals("Атрибут 'source-datasource' источника данных 'inh1' ссылается на несуществующий источник 'ds1' родительской страницы", exc.getMessage());
    }

    /**
     * Проверяется, что действие submit внутри источника данных ссылается на существующий источник данных
     */
    @Test
    public void testTargetDatasourceExistence() {
        N2oMetadataValidationException exc = assertThrows(N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/datasource/testNonExistTargetDatasource.page.xml"));
        assertEquals("Атрибут 'target-datasource' элемента 'submit' источника данных 'inh1' ссылается на несуществующий источник 'ds2'", exc.getMessage());
    }

    /**
     * Проверяется, что действие submit внутри источника данных в действии открытия страницы ссылается на существующий источник данных
     */
    @Test
    public void testTargetDatasourceInPageActionExistence() {
        N2oMetadataValidationException exc = assertThrows(N2oMetadataValidationException.class,
                () -> validate("net/n2oapp/framework/config/metadata/validation/datasource/testNonExistTargetDatasourceInPageAction.page.xml"));
        assertEquals("Атрибут 'target-datasource' элемента 'submit' источника данных 'inh1' ссылается на несуществующий источник 'ds2' родительской страницы", exc.getMessage());
    }
}
