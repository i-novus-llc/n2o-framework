package net.n2oapp.framework.config.reader;

import net.n2oapp.framework.api.exception.N2oException;
import net.n2oapp.framework.api.metadata.SourceMetadata;
import net.n2oapp.framework.api.metadata.reader.NamespaceReaderFactory;
import net.n2oapp.framework.api.reader.SourceLoader;
import net.n2oapp.framework.config.io.MetadataParamHolder;
import net.n2oapp.framework.config.register.XmlInfo;
import net.n2oapp.framework.config.register.route.RouteUtil;
import net.n2oapp.framework.config.util.FileSystemUtil;
import org.apache.commons.io.IOUtils;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static net.n2oapp.framework.config.selective.SelectiveUtil.getSAXBuilder;


/**
 * Чтение XML метаданных
 */
public class XmlMetadataLoader implements SourceLoader<XmlInfo> {

    private NamespaceReaderFactory elementReaderFactory;

    public XmlMetadataLoader(NamespaceReaderFactory elementReaderFactory) {
        this.elementReaderFactory = elementReaderFactory;
    }

    @Override
    public <S extends SourceMetadata> S load(XmlInfo info, String params) {
        Class<? extends SourceMetadata> sourceClass = info.getBaseSourceClass();
        try (InputStream inputStream = FileSystemUtil.getContentAsStream(info.getURI())) {
            MetadataParamHolder.setParams(RouteUtil.parseQueryParams(params));
            S source = read(info.getId(), inputStream);
            if (!sourceClass.isAssignableFrom(source.getClass()))
                throw new MetadataReaderException("read class [" + source.getClass() + "], but expected [" + sourceClass + "]");
            return source;
        } catch (N2oException e) {
            throw e;
        } catch (Exception e) {
            throw new N2oMetadataReaderException(e, info.getId(), info.getURI(), info.getConfigId().getType());
        } finally {
            MetadataParamHolder.setParams(null);
        }
    }

    public <T extends SourceMetadata> T read(String id, String xml) {
        return read(id, IOUtils.toInputStream(xml, StandardCharsets.UTF_8));
    }

    @SuppressWarnings("unchecked")
    public <T extends SourceMetadata> T read(String id, InputStream xml) {
        Document doc;
        try {
            doc = getSAXBuilder().build(xml);
        } catch (JDOMException | IOException e) {
            throw new N2oException("Error reading metadata " + id, e);
        }
        Element root = doc.getRootElement();
        T n2o = (T) elementReaderFactory.produce(root).read(root);
        if (n2o == null)
            throw new MetadataReaderException("Xml Element Reader must return not null object");
        n2o.setId(id);
        return n2o;
    }

    public <T extends SourceMetadata> T read(String id, InputStream xml, Class<T> metadataClass) {
        T n2o = read(id, xml);
        if (!metadataClass.isAssignableFrom(n2o.getClass()))
            throw new MetadataReaderException("read class [" + n2o.getClass() + "], but expected [" + metadataClass + "]");
        return n2o;
    }
}
