package net.n2oapp.framework.config.audit;

import net.n2oapp.framework.config.register.audit.model.N2oConfigConflict;
import net.n2oapp.framework.config.register.audit.util.N2oConfigConflictParser;
import org.apache.commons.io.IOUtils;
import org.custommonkey.xmlunit.Diff;
import org.custommonkey.xmlunit.XMLUnit;
import org.junit.Assert;
import org.junit.Test;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * @author dfirstov
 * @since 03.09.2015
 */
public class N2oConfigConflictParserTest {
    private static final String RESOURCE_PATH = "net/n2oapp/framework/config/audit";

    @Test
    public void testConflictParser() throws IOException, URISyntaxException, SAXException {
        testParse("/conflict/case1/conflictResult.page.xml", "/conflict/case1/parent.page.xml");
        testParse("/conflict/case2/conflictResult.page.xml", "/conflict/case2/parent.page.xml");
        testParse("/conflict/case3/conflictResult.page.xml", "/conflict/case3/parent.page.xml");
    }

    private void testParse(String conflictFilePath, String originFilePath) throws IOException, SAXException {
        URL conflict = this.getClass().getClassLoader().getResource(RESOURCE_PATH + conflictFilePath);
        URL parent = this.getClass().getClassLoader().getResource(RESOURCE_PATH + originFilePath);
        Assert.assertTrue(conflict != null && parent != null);
        N2oConfigConflict configConflict = new N2oConfigConflict();
        configConflict.setConflictContent(IOUtils.toString(conflict, "UTF-8"));
        N2oConfigConflictParser.restoreContentsByConflict(configConflict);
        Diff diff = XMLUnit.compareXML(configConflict.getParentContent(), IOUtils.toString(parent, "UTF-8"));
        Assert.assertTrue(diff.identical());
    }
}
