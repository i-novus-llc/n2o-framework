package net.n2oapp.framework.sandbox.loader;

import net.n2oapp.framework.api.exception.N2oException;
import net.n2oapp.framework.api.metadata.SourceMetadata;
import net.n2oapp.framework.api.metadata.reader.NamespaceReaderFactory;
import net.n2oapp.framework.api.reader.SourceLoader;
import net.n2oapp.framework.api.register.MetadataRegister;
import net.n2oapp.framework.config.io.MetadataParamHolder;
import net.n2oapp.framework.config.reader.MetadataReaderException;
import net.n2oapp.framework.config.register.route.RouteUtil;
import net.n2oapp.framework.sandbox.scanner.ProjectFileInfo;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class ProjectFileLoader implements SourceLoader<ProjectFileInfo> {

    private NamespaceReaderFactory elementReaderFactory;
    private MetadataRegister configRegister;

    public ProjectFileLoader(NamespaceReaderFactory elementReaderFactory) {
        this.elementReaderFactory = elementReaderFactory;
    }

    @Override
    public <S extends SourceMetadata> S load(ProjectFileInfo info, String params) {
        Class<? extends SourceMetadata> sourceClass = info.getBaseSourceClass();
        try (InputStream inputStream = new ByteArrayInputStream(info.getSource().getBytes())) {
            MetadataParamHolder.setParams(RouteUtil.parseQueryParams(params));
            S source = read(info.getId(), inputStream);
            if (!sourceClass.isAssignableFrom(source.getClass()))
                throw new MetadataReaderException("read class [" + source.getClass() + "], but expected [" + sourceClass + "]");
            return source;
        } catch (Exception e) {
            throw new N2oException(e);
        } finally {
            MetadataParamHolder.setParams(null);
        }
    }

    @SuppressWarnings("unchecked")
    public <T extends SourceMetadata> T read(String id, InputStream xml) {
        SAXBuilder builder = new SAXBuilder();
        Document doc;
        try {
            doc = builder.build(xml);
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
}
