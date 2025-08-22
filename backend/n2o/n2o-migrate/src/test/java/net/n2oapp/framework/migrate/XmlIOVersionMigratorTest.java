package net.n2oapp.framework.migrate;

import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.pack.N2oAllIOPack;
import net.n2oapp.framework.config.test.N2oTestBase;
import net.n2oapp.framework.config.util.FileSystemUtil;
import org.custommonkey.xmlunit.Diff;
import org.custommonkey.xmlunit.ElementNameQualifier;
import org.custommonkey.xmlunit.XMLUnit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.util.function.BiFunction;

import static org.junit.jupiter.api.Assertions.assertTrue;

class XmlIOVersionMigratorTest extends N2oTestBase {
    private static final Logger logger = LoggerFactory.getLogger(XmlIOVersionMigratorTest.class);
    private static final BiFunction<String, String, Boolean> CANONICAL_COMPARATOR = (String s1, String s2) -> {
        try {
            Diff diff = XMLUnit.compareXML(s1, s2);
            diff.overrideElementQualifier(new ElementNameQualifier());
            boolean similar = diff.similar();
            logger.info("""
                    Comparing two xml...
                    Source:
                    {}
                    Persisted:
                    {}
                    Similar? {}
                    Identical? {}
                    """, s2, s1, similar, diff.identical());
            logger.info(diff.toString());
            return similar;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    };
    private XmlIOVersionMigrator migrator;

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oAllIOPack());
        migrator = new XmlIOVersionMigrator(builder);
    }

    /**
     * Тест на миграцию query.xml с версии 4.0 на 5.0
     */
    @Test
    void testQueryMigration() {
        check("net/n2oapp/framework/migrate/query/testXmlIOVersionMigratorTestQueryIOv4.query.xml",
                "net/n2oapp/framework/migrate/query/testXmlIOVersionMigratorTestQueryIOv5.query.xml");
    }

    /**
     * Тест на миграцию application.xml с версии 2.0 на 3.0
     */
    @Test
    void testApplicationMigration() {
        check("net/n2oapp/framework/migrate/application/testXmlIOVersionMigratorTestApplicationIOv2.application.xml",
                "net/n2oapp/framework/migrate/application/testXmlIOVersionMigratorTestApplicationIOv3.application.xml");
    }

    /**
     * Тест на миграцию simple-page.xml с версии 3.0 на 4.0
     */
    @Test
    void testSimplePageMigration() {
        check("net/n2oapp/framework/migrate/page/simple/testXmlIOVersionMigratorTestSimplePageIOv3.page.xml",
                "net/n2oapp/framework/migrate/page/simple/testXmlIOVersionMigratorTestSimplePageIOv4.page.xml");
    }

    /**
     * Тест на миграцию page.xml с версии 3.0 на 4.0
     */
    @Test
    void testStandardPageMigration() {
        check("net/n2oapp/framework/migrate/page/standard/testXmlIOVersionMigratorTestStandardPageIOv3.page.xml",
                "net/n2oapp/framework/migrate/page/standard/testXmlIOVersionMigratorTestStandardPageIOv4.page.xml");
    }

    /**
     * Тест миграции опциональных атрибутов с версии page-3.0 нв page-4.0 (control-2.0 на control-3.0)
     */
    @Test
    void testOtherAttributesMigration() {
        check("net/n2oapp/framework/migrate/page/control/options/testXmlIOVersionMigratorTestControlOptionsIOv3.page.xml",
                "net/n2oapp/framework/migrate/page/control/options/testXmlIOVersionMigratorTestControlOptionsIOv4.page.xml");
    }

    /**
     * Тест на миграцию table.widget.xml с версии 4.0 на 5.0
     */
    @Test
    void testTableWidgetMigration() {
        check("net/n2oapp/framework/migrate/widget/table/testXmlIOVersionTestTableIOv4.widget.xml",
                "net/n2oapp/framework/migrate/widget/table/testXmlIOVersionTestTableIOv5.widget.xml");
    }

    /**
     * Тест на миграцию form.widget.xml с версии 4.0 на 5.0
     */
    @Test
    void testFormWidgetMigration() {
        check("net/n2oapp/framework/migrate/widget/form/testXmlIoVersionMigratorTestFormIOv4.widget.xml",
                "net/n2oapp/framework/migrate/widget/form/testXmlIoVersionMigratorTestFormIOv5.widget.xml");
    }

    /**
     * Тест на миграцию tree.widget.xml с версии 4.0 на 5.0
     */
    @Test
    void testTreeWidgetMigration() {
        check("net/n2oapp/framework/migrate/widget/tree/testXmlIOVersionTestTreeIOv4.widget.xml",
                "net/n2oapp/framework/migrate/widget/tree/testXmlIOVersionTestTreeIOv5.widget.xml");
    }

    /**
     * Тест на миграцию calendar.widget.xml с версии 4.0 на 5.0
     */
    @Test
    void testCalendarWidgetMigration() {
        check("net/n2oapp/framework/migrate/widget/calendar/testXmlIOVersionTestMigratorIOv4.widget.xml",
                "net/n2oapp/framework/migrate/widget/calendar/testXmlIOVersionTestMigratorIOv5.widget.xml");
    }

    /**
     * Тест на миграцию cards.widget.xml с версии 4.0 на 5.0
     */
    @Test
    void testCardsWidgetMigration() {
        check("net/n2oapp/framework/migrate/widget/cards/testXmlIOVersionTestMigratorIOv4.widget.xml",
                "net/n2oapp/framework/migrate/widget/cards/testXmlIOVersionTestMigratorIOv5.widget.xml");
    }

    /**
     * Тест на миграцию html.widget.xml с версии 4.0 на 5.0
     */
    @Test
    void testHtmlWidgetMigration() {
        check("net/n2oapp/framework/migrate/widget/html/testXmlIOVersionTestMigratorIOv4.widget.xml",
                "net/n2oapp/framework/migrate/widget/html/testXmlIOVersionTestMigratorIOv5.widget.xml");
    }


    private void check(String oldVersionXmlPath, String newVersionXmlPath) {
        String oldContent = FileSystemUtil.getContentFromResource(new ClassPathResource(oldVersionXmlPath));
        String newContent = FileSystemUtil.getContentFromResource(new ClassPathResource(newVersionXmlPath));
        String migratedContent = migrator.migrate(oldContent);
        assertTrue(CANONICAL_COMPARATOR.apply(migratedContent, newContent));
    }

    /**
     * Миграция файлов application.xml версии 2.0 и query.xml версии 4.0
     * Метод используется для изменения\миграции файлов проекта!
     */
    @Test
    @Disabled("Используется исключительно для миграции файлов проекта!")
    void migrateFiles() throws IOException {
        boolean result = migrator.migrateFiles(".application.xml",
                "http://n2oapp.net/framework/config/schema/application-2.0",
                "src/test/resources/");
        assertTrue(result);
    }
}