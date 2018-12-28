package net.n2oapp.framework.config.persister.control;

import net.n2oapp.framework.api.metadata.control.N2oCodeMerge;
import net.n2oapp.framework.config.persister.util.PersisterJdomUtil;
import org.jdom.Element;
import org.springframework.stereotype.Component;
import org.jdom.Namespace;

/**
 * User: dfirstov
 * Date: 22.05.15
 * Time: 16:52
 */
@Component
public class N2oCodeMergePersister extends N2oControlXmlPersister<N2oCodeMerge> {
    @Override
    public Element persist(N2oCodeMerge n2o, Namespace namespace) {
        Element n2OCodeMerge = new Element(getElementName(), namespacePrefix, namespaceUri);
        PersisterJdomUtil.setAttribute(n2OCodeMerge, "merge-view", n2o.getMergeView());
        PersisterJdomUtil.setAttribute(n2OCodeMerge, "show-differences", n2o.getShowDifferences());
        PersisterJdomUtil.setAttribute(n2OCodeMerge, "connect-align", n2o.getConnectAlign());
        PersisterJdomUtil.setAttribute(n2OCodeMerge, "collapse-identical", n2o.getCollapseIdentical());
        PersisterJdomUtil.setAttribute(n2OCodeMerge, "allow-editing-originals", n2o.getAllowEditingOriginals());
        PersisterJdomUtil.setAttribute(n2OCodeMerge, "language", n2o.getLanguage());
        PersisterJdomUtil.setAttribute(n2OCodeMerge, "rows", n2o.getRows().toString());
        setControl(n2OCodeMerge, n2o);
        setField(n2OCodeMerge, n2o);
        return n2OCodeMerge;
    }

    @Override
    public Class<N2oCodeMerge> getElementClass() {
        return N2oCodeMerge.class;
    }

    @Override
    public String getElementName() {
        return "code-merge";
    }
}
