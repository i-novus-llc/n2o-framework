package net.n2oapp.framework.migrate;

import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.pack.N2oAllIOPack;
import net.n2oapp.framework.config.test.N2oTestBase;
import net.n2oapp.framework.config.util.FileSystemUtil;
import org.custommonkey.xmlunit.Diff;
import org.custommonkey.xmlunit.ElementNameQualifier;
import org.custommonkey.xmlunit.XMLUnit;
import org.junit.jupiter.api.BeforeEach;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

import java.util.function.BiFunction;

import static org.junit.jupiter.api.Assertions.assertTrue;

class XmlIOVersionMigratorTest extends N2oTestBase {
    private static final Logger logger = LoggerFactory.getLogger(XmlIOVersionMigratorTest.class);
    private static final BiFunction<String, String, Boolean> CANONICAL_COMPARATOR = (String s1, String s2) -> {
        try {
            XMLUnit.setIgnoreWhitespace(true);
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

    private void check(String oldVersionXmlPath, String newVersionXmlPath) {
        String oldContent = FileSystemUtil.getContentFromResource(new ClassPathResource(oldVersionXmlPath));
        String newContent = FileSystemUtil.getContentFromResource(new ClassPathResource(newVersionXmlPath));
        String migratedContent = migrator.migrate(oldContent);
        assertTrue(CANONICAL_COMPARATOR.apply(migratedContent, newContent));
    }
}