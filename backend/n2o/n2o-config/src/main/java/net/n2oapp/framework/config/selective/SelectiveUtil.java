package net.n2oapp.framework.config.selective;

import net.n2oapp.framework.api.metadata.SourceMetadata;
import net.n2oapp.framework.api.metadata.reader.ElementReaderFactory;
import net.n2oapp.framework.api.metadata.reader.NamespaceReaderFactory;
import net.n2oapp.framework.config.util.FileSystemUtil;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;

/**
 * User: operehod
 * Date: 13.02.2015
 * Time: 14:59
 */
public class SelectiveUtil {

    public static <N extends SourceMetadata> N readByPath(String id, String path, NamespaceReaderFactory readerFactory) {
        N n2o = readByPath(path, readerFactory);
        n2o.setId(id);
        return n2o;
    }

    @SuppressWarnings("unchecked")
    public static <N> N readByPath(String uri, NamespaceReaderFactory readerFactory) {
        try (InputStream inputStream = FileSystemUtil.getContentAsStream(uri)) {
            SAXBuilder builder = new SAXBuilder();
            Document doc = builder.build(inputStream);
            Element root = doc.getRootElement();
            return (N) readerFactory.produce(root).read(root);
        } catch (JDOMException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unchecked")
    public static <N > N read(String source, ElementReaderFactory readerFactory) {
        try (Reader stringReader = new StringReader(source)) {
            SAXBuilder builder = new SAXBuilder();
            Document doc = builder.build(stringReader);
            Element root = doc.getRootElement();
            return (N) readerFactory.produce(root).read(root);
        } catch (JDOMException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}
