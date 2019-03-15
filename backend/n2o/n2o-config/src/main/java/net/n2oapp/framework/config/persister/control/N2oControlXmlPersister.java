package net.n2oapp.framework.config.persister.control;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.n2oapp.framework.api.exception.N2oException;
import net.n2oapp.framework.api.metadata.control.N2oField;
import net.n2oapp.framework.api.metadata.control.N2oListField;
import net.n2oapp.framework.api.metadata.control.N2oStandardField;
import net.n2oapp.framework.api.metadata.control.interval.N2oIntervalField;
import net.n2oapp.framework.api.metadata.control.list.N2oSelectTree;
import net.n2oapp.framework.api.metadata.control.plain.N2oText;
import net.n2oapp.framework.api.metadata.global.dao.N2oPreFilter;
import net.n2oapp.framework.api.metadata.global.view.widget.tree.GroupingNodes;
import net.n2oapp.framework.api.metadata.global.view.widget.tree.InheritanceNodes;
import net.n2oapp.framework.api.metadata.persister.AbstractN2oMetadataPersister;
import net.n2oapp.framework.config.persister.tools.PreFilterPersister;
import net.n2oapp.framework.config.persister.util.CssClassAwarePersister;
import net.n2oapp.framework.config.persister.util.PersisterJdomUtil;
import org.jdom.Element;
import org.jdom.Namespace;

import java.util.List;
import java.util.Map;

import static net.n2oapp.framework.config.persister.util.PersisterJdomUtil.*;

/**
 * Абстрактная реализация сохранения контрола в xml-файл
 */
public abstract class N2oControlXmlPersister<T extends N2oField> extends AbstractN2oMetadataPersister<T> {

    private ObjectMapper mapper = new ObjectMapper();

    public N2oControlXmlPersister() {
        super("http://n2oapp.net/framework/config/schema/n2o-control-1.0", "ctrl");
    }

    @Override
    public void setNamespaceUri(String namespaceUri) {
        this.namespaceUri = namespaceUri;
    }

    @Override
    public void setNamespacePrefix(String namespacePrefix) {
        this.namespacePrefix = namespacePrefix;
    }

    protected void setControl(Element element, N2oField n2oControl) {
        if (n2oControl.getLabel() != null)
            element.setAttribute("label", n2oControl.getLabel());
        setAttribute(element, "visible", n2oControl.getVisible());
        setElementString(element, "description", n2oControl.getDescription());
        element.setAttribute("id", n2oControl.getId());
        if (n2oControl instanceof N2oStandardField) {
            setElementString(element, "placeholder", ((N2oStandardField) n2oControl).getPlaceholder());
        }
    }


    @SuppressWarnings("unchecked")
    protected void setListField(Element element, N2oListField n2oField) {
        if (n2oField.getCache() != null) {
            if (n2oField.getCache())
                setAttribute(element, "cache", "on");
            else if (!n2oField.getCache())
                setAttribute(element, "cache", "off");
        }

        setAttribute(element, "autoselect-alone", n2oField.getAutoselectAlone());
        setAttribute(element, "autoselect-first", n2oField.getAutoselectFirst());
        setAttribute(element, "popup-scaling", n2oField.getPopupScaling());
        setAttribute(element, "size", n2oField.getSize());
        setDefaultModel(element, n2oField);
    }

    @SuppressWarnings("unchecked")
    protected void setTreeField(Element element, N2oSelectTree selectTree) {
        setAttribute(element, "ajax", selectTree.getAjax());
        setAttribute(element, "search", selectTree.getSearch());
        setAttribute(element, "checkboxes", selectTree.getCheckboxes());
        if (selectTree.getInheritanceNodes() != null) {
            InheritanceNodes in = selectTree.getInheritanceNodes();
            Element inheritanceNodes = new Element("inheritance-nodes", namespacePrefix, namespaceUri);
            inheritanceNodes.setAttribute("parent-field-id", in.getParentFieldId());
            inheritanceNodes.setAttribute("label-field-id", in.getLabelFieldId());
            setAttribute(inheritanceNodes, "value-field-id", in.getValueFieldId());
            setAttribute(inheritanceNodes, "detail-field-id", in.getDetailFieldId());
            setAttribute(inheritanceNodes, "master-field-id", in.getMasterFieldId());
            setAttribute(inheritanceNodes, "query-id", in.getQueryId());
            setAttribute(inheritanceNodes, "has-children-field-id", in.getHasChildrenFieldId());
            setAttribute(inheritanceNodes, "icon-field-id", in.getIconFieldId());
            setAttribute(inheritanceNodes, "search-field-id", in.getSearchFieldId());
            setAttribute(inheritanceNodes, "enabled-field-id", in.getEnabledFieldId());
            PreFilterPersister.setPreFilter(selectTree.getInheritanceNodes().getPreFilters(), inheritanceNodes, Namespace.getNamespace(namespacePrefix, namespaceUri));
            element.addContent(inheritanceNodes);
        } else if (selectTree.getGroupingNodes() != null) {
            List<GroupingNodes.Node> nodes = selectTree.getGroupingNodes().getNodes();
            Element groupingNodes = new Element("grouping-nodes", namespacePrefix, namespaceUri);
            groupingNodes.setAttribute("query-id", selectTree.getGroupingNodes().getQueryId());
            setAttribute(groupingNodes, "detail-field-id", selectTree.getGroupingNodes().getDetailFieldId());
            setAttribute(groupingNodes, "label-field-id", selectTree.getGroupingNodes().getDetailFieldId());
            setAttribute(groupingNodes, "master-field-id", selectTree.getGroupingNodes().getMasterFieldId());
            setAttribute(groupingNodes, "value-field-id", selectTree.getGroupingNodes().getValueFieldId());
            if (selectTree.getGroupingNodes().getSearchFieldId() != null)
                groupingNodes.setAttribute("search-field-id", selectTree.getGroupingNodes().getSearchFieldId());
            element.addContent(groupingNodes);
            createNode(nodes.get(0), namespaceUri, groupingNodes);
            PreFilterPersister.setPreFilter(selectTree.getGroupingNodes().getPreFilters(), groupingNodes, Namespace.getNamespace(namespacePrefix, namespaceUri));
        }
    }

    private void createNode(GroupingNodes.Node node, String namespace, Element root) {
        Element nodeElement = new Element("node", namespace);
        nodeElement.setAttribute("value-field-id", node.getValueFieldId());
        nodeElement.setAttribute("label-field-id", node.getLabelFieldId());
        if (node.getIcon() != null)
            nodeElement.setAttribute("icon", node.getIcon());
        if (node.getEnabled() != null)
            nodeElement.setAttribute("enabled", String.valueOf(node.getEnabled()));
        root.addContent(nodeElement);
        if (node.getNodes() != null && node.getNodes().size() > 0)
            createNode(node.getNodes().get(0), namespace, nodeElement);
    }


    @SuppressWarnings("unchecked")
    protected void setField(Element element, N2oField n2oField) {
        if (n2oField.getDependsOn() != null) {
            setAttribute(element, "depends-on", n2oField.getDependsOn()[0]);
        }
        setAttribute(element, "dependency-condition", persistDependencyCondition(n2oField));
        setAttribute(element, "domain", n2oField.getDomain());
        setAttribute(element, "required", n2oField.getRequired());
        setAttribute(element, "label-style", n2oField.getLabelStyle());
        if (n2oField instanceof N2oStandardField) {
            setAttribute(element, "copied", ((N2oStandardField) n2oField).getCopied());
            setValidations((N2oStandardField)n2oField, element);
            if (((N2oStandardField)n2oField).getDefaultValue() != null)
                setAttribute(element, "default-value", ((N2oStandardField)n2oField).getDefaultValue());
        }
        setAttribute(element, "control-style", n2oField.getStyle());
        setAttribute(element, "src", n2oField.getSrc());
        CssClassAwarePersister.getInstance().persist(element, n2oField);
        setDependencies(n2oField, element);
    }

    protected void setText(Element element, N2oText n2oField) {
        PersisterJdomUtil.setAttribute(element, "rows", n2oField.getRows());
        PersisterJdomUtil.setAttribute(element, "height", n2oField.getHeight());
    }

    protected void setListQuery(Element element, N2oListField control) {
        if (control.getQueryId() == null) return;
        Element queryEl = new Element("query", namespacePrefix, namespaceUri);
        element.addContent(queryEl);

        PersisterJdomUtil.setAttribute(queryEl, "query-id", control.getQueryId());
        PersisterJdomUtil.setAttribute(queryEl, "master-field-id", control.getMasterFieldId());
        PersisterJdomUtil.setAttribute(queryEl, "detail-field-id", control.getDetailFieldId());
        PersisterJdomUtil.setAttribute(queryEl, "label-field-id", control.getLabelFieldId());
        PersisterJdomUtil.setAttribute(queryEl, "search-field-id", control.getSearchFieldId());
        PersisterJdomUtil.setAttribute(queryEl, "value-field-id", control.getValueFieldId());
        PersisterJdomUtil.setAttribute(queryEl, "format", control.getFormat());
        PersisterJdomUtil.setAttribute(queryEl, "icon-field-id", control.getIconFieldId());
        PreFilterPersister.setPreFilter(control.getPreFilters(), queryEl, queryEl.getNamespace());
    }

    protected void setDefaultModel(Element element, N2oField listField) {
        if (listField instanceof N2oListField) {
            if (((N2oListField) listField).getDefValue() == null) return;
            Element defaultModel = setEmptyElement(element, "default-model");
            ((N2oListField) listField).getDefValue().forEach((field, val) -> {
                Element value = setEmptyElement(defaultModel, "value");
                setAttribute(value, "field-id", field);
                value.setText(val);
            });
        } else if (listField instanceof N2oIntervalField) {
            if (((N2oIntervalField) listField).getBegin() == null && ((N2oIntervalField) listField).getEnd() == null)
                return;
            Element defaultModel = setEmptyElement(element, "default-model");
            setAttribute(defaultModel, "begin", ((N2oIntervalField) listField).getBegin());
            setAttribute(defaultModel, "end", ((N2oIntervalField) listField).getEnd());
        }
    }

    protected void setOptionsList(Element element, Map<String, String>[] options) {
        if (options == null) return;

        Element optElement = new Element("options", namespacePrefix, namespaceUri);
        element.addContent(optElement);
        for (Map<String, String> option : options) {
            Element itemElement = new Element("option", namespacePrefix, namespaceUri);
            String json;
            try {
                json = mapper.writer().writeValueAsString(option);
            } catch (JsonProcessingException e) {
                throw new N2oException(e);
            }

            itemElement.setText(json);

            optElement.addContent(itemElement);
        }
    }

    protected Element getPreFilter(List<N2oPreFilter> preFilters) {
        if (preFilters != null && preFilters.size() > 0) {
            Element preFiltersElement = new Element("pre-filters", namespacePrefix, namespaceUri);
            for (N2oPreFilter n2oPreFilter : preFilters) {
                Element preFilterElement = new Element("pre-filter", namespacePrefix, namespaceUri);
                preFiltersElement.addContent(preFilterElement);
                if (n2oPreFilter.getFieldId() != null)
                    preFilterElement.setAttribute("field-id", n2oPreFilter.getFieldId());
                if (n2oPreFilter.getRef() != null)
                    preFilterElement.setAttribute("ref", n2oPreFilter.getRef());
                if (n2oPreFilter.getValue() != null)
                    preFilterElement.setAttribute("value", n2oPreFilter.getValue());
                if (n2oPreFilter.getType() != null)
                    preFilterElement.setAttribute("type", n2oPreFilter.getType().name());
                if (n2oPreFilter.getValues() != null) {
                    for (String value : n2oPreFilter.getValues())
                        setElementString(preFilterElement, "value", value);
                }
            }
            return preFiltersElement;
        }
        return null;
    }

    private String persistDependencyCondition(N2oField n2oField) {
        if (n2oField.getDependencies() == null) return null;
        for (N2oField.Dependency dependency : n2oField.getDependencies()) {
            if (dependency instanceof N2oField.VisibilityDependency) {
                return dependency.getValue();
            }
        }
        return null;
    }

    private void setValidations(N2oStandardField field, Element rootElement) {
        if (field.getValidations() == null || field.getValidations().getWhiteList() == null) return;
        Element validations = setEmptyElement(rootElement, "validations");

        for (String reference : field.getValidations().getWhiteList()) {
            Element validation = setEmptyElement(validations, "validation");
            setAttribute(validation, "ref-id", reference);
        }
//        for (Map.Entry<String, ValidationReference> entry : field.getValidationReferences().entrySet()) {
//            ValidationReference value = entry.getValue();
//            Element validation = setEmptyElement(validations, "validation");
//            setAttribute(validation, "ref-id", value.getReference());
//            setAttribute(validation, "side", value.getSide());
//        }
    }

    private void setDependencies(N2oField field, Element rootElement) {
        if (field.getDependencies() != null) {
            Element dependencies = null;
            for (N2oField.Dependency dependency : field.getDependencies()) {
                if (dependency instanceof N2oField.EnablingDependency) {
                    if (dependencies == null)
                        dependencies = setEmptyElement(rootElement, "dependencies");
                    Element enable = setElementString(dependencies, "enabling-condition", dependency.getValue());
                    if (dependency.getOn() != null) {
                        String on = String.join(",", dependency.getOn());
                        setAttribute(enable, "on", on);
                    }
                } else if (dependency instanceof N2oField.RequiringDependency) {
                    if (dependencies == null)
                        dependencies = setEmptyElement(rootElement, "dependencies");
                    Element required = setElementString(dependencies, "required-condition", dependency.getValue());
                    if (dependency.getOn() != null) {
                        String on = String.join(",", dependency.getOn());
                        setAttribute(required, "on", on);
                    }
                } else if (dependency instanceof N2oField.SetValueDependency) {
                    Element setValueExp = setElementString(rootElement, "set-value-expression", dependency.getValue());
                    if (dependency.getOn() != null) {
                        String on = String.join(",", dependency.getOn());
                        setAttribute(setValueExp, "on", on);
                    }
                }
            }
        }
    }
}



