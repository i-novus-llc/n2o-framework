package net.n2oapp.framework.config.util;

import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.junit.Assert;

import java.io.InputStream;
import java.util.List;

/**
 * @author iryabov
 * @since 10.02.2015
 */
public class PersisterTestUtil {

    private static Element getRootElement(InputStream xml)
    {
        SAXBuilder builder = new SAXBuilder();
        Document doc = null;
        try
        {
            doc = builder.build(xml);
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
        return doc.getRootElement();
    }

    public static void assertCanonicalElement(Element expected, Element actual)
    {
        assert expected.getName().equals(actual.getName()) :
                expected + " element expected [" + expected.getName() + "], but actual [" + actual.getName() + "]";
        assert expected.getAttributes().size() >= actual.getAttributes().size() :
                expected + " attributes expected " + expected.getAttributes().size() + ", but actual " + actual.getAttributes().size();
        for (Attribute expectedAttribute : (List<Attribute>)expected.getAttributes())
        {
            Attribute actualAttribute = actual.getAttribute(expectedAttribute.getName(), expectedAttribute.getNamespace());
            assert null != actualAttribute
                    : expected + " expected attribute [" + expectedAttribute.getName() + "]";
            Assert.assertEquals(expectedAttribute.getValue().trim(), actualAttribute.getValue().trim());
        }
        assert expected.getChildren().size() == actual.getChildren().size(): "children elements expected [" + expected.getChildren().size() + "], but actual [" + actual.getChildren().size() + "]";
        if (expected.getChildren().isEmpty())
        {
            Assert.assertEquals(expected.getValue().trim(), actual.getValue().trim());
        }
        for (int k = 0; k < expected.getChildren().size(); k++)
        {
            Element expectedChildElement = (Element)expected.getChildren().get(k);
            Element actualChildElement = (Element)actual.getChildren().get(k);
            assertCanonicalElement(expectedChildElement, actualChildElement);
        }
    }


    public static void assertCanonicalXml(String expectedXmlFile, InputStream actualXml)
    {
        Element actualRoot = getRootElement(actualXml);
        assertCanonicalXml(expectedXmlFile, actualRoot);
    }

    public static void assertCanonicalXml(String expectedXmlFile, Element actualRoot) {
        InputStream input = PersisterTestUtil.class.getClassLoader().getResourceAsStream(expectedXmlFile);
        if (input == null)
            throw new IllegalArgumentException("xml file [" + expectedXmlFile + "] not found");
        Element expectedRoot = getRootElement(input);
        assertCanonicalElement(expectedRoot, actualRoot);
    }

}
