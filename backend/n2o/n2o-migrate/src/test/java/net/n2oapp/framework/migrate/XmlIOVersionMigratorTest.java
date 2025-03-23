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
    private XmlIOVersionMigrator migrator;
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

    @Override
    @BeforeEach
    public void setUp() throws Exception {
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