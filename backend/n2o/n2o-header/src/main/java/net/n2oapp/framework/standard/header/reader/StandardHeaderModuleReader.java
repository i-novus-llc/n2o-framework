package net.n2oapp.framework.standard.header.reader;

import net.n2oapp.framework.api.metadata.reader.AbstractFactoredReader;
import net.n2oapp.framework.config.reader.tools.PropertiesReaderV1;
import net.n2oapp.framework.config.reader.util.ReaderJdomUtil;
import net.n2oapp.framework.standard.header.model.global.N2oBaseHeaderModule;
import net.n2oapp.framework.standard.header.model.global.N2oHeaderModule;
import org.jdom.Element;
import org.jdom.Namespace;

import java.util.List;

/**
 * User: operhod
 * Date: 17.02.14
 * Time: 10:18
 */
public class StandardHeaderModuleReader extends AbstractFactoredReader<N2oHeaderModule> {
    @Override
    public N2oHeaderModule read(Element element, Namespace namespace) {
        N2oBaseHeaderModule headerModule = new N2oBaseHeaderModule();
        String refId = ReaderJdomUtil.getAttributeString(element, "ref-id");
        if (refId != null) {
            headerModule.setRefId(refId);
            return headerModule;
        }
        headerModule.setProperties(PropertiesReaderV1.getInstance().read(element, namespace));
        headerModule.setName(ReaderJdomUtil.getAttributeString(element, "name"));
        headerModule.setSourceId(ReaderJdomUtil.getAttributeString(element, "id"));
        headerModule.setUrl(ReaderJdomUtil.getAttributeString(element, "url"));
        headerModule.setMainPage(ReaderJdomUtil.getAttributeString(element, "main-page"));
        headerModule.setItems(readItems(element.getChildren(), namespace));
        return headerModule;
    }

    private N2oBaseHeaderModule.Item[] readItems(List<Element> children, Namespace namespace) {
        if (children == null || children.size() < 1) return null;
        N2oBaseHeaderModule.Item[] res = new N2oBaseHeaderModule.Item[children.size()];
        int i = 0;
        for (Element el : children) {
            String name = el.getName();
            switch (name) {
                case "space":
                    i = readSpace(namespace, res, i, el);
                    break;
                case "page":
                    i = readPage(res, i, el);
                    break;
            }
        }
        return res;
    }

    private static int readSpace(Namespace namespace, N2oBaseHeaderModule.Item[] res, int i, Element el) {
        N2oBaseHeaderModule.Space space = new N2oBaseHeaderModule.Space();
        space.setName(ReaderJdomUtil.getAttributeString(el, "name"));
        space.setId(ReaderJdomUtil.getAttributeString(el, "id"));
        space.setMainPage(ReaderJdomUtil.getAttributeString(el, "main-page"));
        space.setPages(readPages(el.getChildren("page", namespace), namespace));
        res[i++] = space;
        return i;
    }

    private static int readPage(N2oBaseHeaderModule.Item[] res, int i, Element el) {
        N2oBaseHeaderModule.Page page = new N2oBaseHeaderModule.Page();
        page.setName(ReaderJdomUtil.getAttributeString(el, "name"));
        page.setId(ReaderJdomUtil.getAttributeString(el, "id"));
        res[i++] = page;
        return i;
    }

    private static N2oBaseHeaderModule.Page[] readPages(List<Element> pages, Namespace namespace) {
        if (pages == null || pages.size() < 1) return null;
        N2oBaseHeaderModule.Page[] res = new N2oBaseHeaderModule.Page[pages.size()];
        int i = 0;
        for (Element el : pages) {
            i = readPage(res, i, el);
        }
        return res;
    }

    @Override
    public Class<N2oHeaderModule> getElementClass() {
        return N2oHeaderModule.class;
    }

    @Override
    public String getNamespaceUri() {
        return "http://n2oapp.net/framework/config/schema/standard-header-module-1.0";
    }

    @Override
    public String getElementName() {
        return "module";
    }
}
