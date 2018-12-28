package net.n2oapp.framework.config.selective;

import net.n2oapp.framework.api.metadata.SourceMetadata;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import net.n2oapp.framework.api.metadata.io.NamespaceIO;
import net.n2oapp.framework.api.metadata.io.TypedElementIO;
import net.n2oapp.framework.api.metadata.reader.ElementReaderFactory;
import net.n2oapp.framework.api.metadata.reader.NamespaceReader;
import net.n2oapp.framework.api.metadata.reader.NamespaceReaderFactory;
import net.n2oapp.framework.config.selective.reader.ReaderFactoryByMap;
import net.n2oapp.framework.config.util.FileSystemUtil;
import org.apache.commons.io.IOUtils;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import net.n2oapp.framework.api.metadata.global.N2oMetadata;

import java.io.*;
import java.util.stream.Collectors;

/**
 * User: operehod
 * Date: 13.02.2015
 * Time: 14:59
 */
public class SelectiveUtil {

    public static <N extends N2oMetadata> N readByPath(String id, String path, NamespaceReader<N> reader) {
        N n2o = readByPath(path, reader);
        n2o.setId(id);
        return n2o;
    }

    public static <N extends SourceMetadata> N readByPath(String id, String path, NamespaceReaderFactory readerFactory) {
        N n2o = readByPath(path, readerFactory);
        n2o.setId(id);
        return n2o;
    }

    @SuppressWarnings("unchecked")
    public static <N extends N2oMetadata> N readByPath(String uri, NamespaceReader<N> reader) {
        if (reader instanceof DummyFactoredReader)
            return (N) ((DummyFactoredReader) reader).getMetadata();
        try (InputStream inputStream = FileSystemUtil.getContentAsStream(uri)) {
            SAXBuilder builder = new SAXBuilder();
            Document doc = builder.build(inputStream);
            Element root = doc.getRootElement();
            return reader.read(root);
        } catch (JDOMException | IOException e) {
            throw new RuntimeException(e);
        }
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

    public static <N> N readByFile(File file, ElementReaderFactory readerFactory) {
        try(FileInputStream fileInputStream = new FileInputStream(file)) {
            String source = IOUtils.readLines(fileInputStream).stream().collect(Collectors.joining());
            return read(source, readerFactory);
        } catch (IOException e) {
            return null;
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

    public static void registerReader(ReaderFactoryByMap readerFactory, NamespaceReader reader) {
        readerFactory.register(reader);
    }

    public static void registerReader(ReaderFactoryByMap readerFactory, NamespaceIO io) {
        readerFactory.register(io);
    }

}
