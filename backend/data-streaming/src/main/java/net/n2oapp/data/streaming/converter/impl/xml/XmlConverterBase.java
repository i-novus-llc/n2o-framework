package net.n2oapp.data.streaming.converter.impl.xml;

import net.n2oapp.data.streaming.DataStreamingUtil;
import net.n2oapp.data.streaming.converter.EncodingAware;
import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import net.n2oapp.data.streaming.converter.Converter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.AbstractMap;
import java.util.Collection;
import java.util.Map;

import static net.n2oapp.data.streaming.DataStreamingUtil.getBytes;

/**
 * Created with IntelliJ IDEA.
 * User: operhod
 * Date: 13.09.13
 * Time: 18:17
 * To change this template use File | Settings | File Templates.
 */
public abstract class XmlConverterBase<T> implements Converter<T>, EncodingAware {

    protected XMLOutputter xmlOutputter = new XMLOutputter();

    protected String xmlRootElementName = "rows";
    protected String xmlElementName = "row";
    protected String encoding = DataStreamingUtil.ENCODING;


    public static class Attribute extends AbstractMap.SimpleEntry<String, String> {

        public Attribute(String key, String value) {
            super(key, value);
        }

        public Attribute(Map.Entry<? extends String, ? extends String> entry) {
            super(entry);
        }
    }


    @Override
    public byte[] convert(T t) {
        Element element = new Element(xmlElementName);
        for (Attribute attribute : retrieveRows(t)) {
            if (attribute != null)
                element.setAttribute(attribute.getKey(), attribute.getValue());
        }
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            xmlOutputter.output(element, out);
            return out.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected abstract Collection<Attribute> retrieveRows(T t);

    @Override
    public byte[] getSeparator() {
        return DataStreamingUtil.EMPTY_BYTE_ARRAY;
    }

    @Override
    public byte[] getOpening() {
        return DataStreamingUtil.getBytes("<?xml version=\"1.0\" encoding=\"" + encoding + "\"?>" + "<" + xmlRootElementName + ">");
    }

    @Override
    public byte[] getClosing() {
        return DataStreamingUtil.getBytes("</" + xmlRootElementName + ">");
    }

    @Override
    public void setEncoding(String encoding) {
        Format rawFormat = Format.getRawFormat();
        rawFormat.setEncoding(encoding);
        xmlOutputter.setFormat(rawFormat);
        this.encoding = encoding;
    }

    public void setXmlRootElementName(String xmlRootElementName) {
        this.xmlRootElementName = xmlRootElementName;
    }

    public void setXmlElementName(String xmlElementName) {
        this.xmlElementName = xmlElementName;
    }
}
