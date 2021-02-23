package net.n2oapp.framework.config.reader.control;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.n2oapp.framework.api.exception.N2oException;
import net.n2oapp.framework.api.metadata.aware.NamespaceUriAware;
import net.n2oapp.framework.api.metadata.control.N2oActionButton;
import net.n2oapp.framework.api.metadata.control.N2oField;
import net.n2oapp.framework.api.metadata.control.N2oListField;
import net.n2oapp.framework.api.metadata.control.N2oStandardField;
import net.n2oapp.framework.api.metadata.control.interval.N2oSimpleIntervalField;
import net.n2oapp.framework.api.metadata.control.list.Inlineable;
import net.n2oapp.framework.api.metadata.control.list.N2oSelectTree;
import net.n2oapp.framework.api.metadata.reader.AbstractFactoredReader;
import net.n2oapp.framework.api.metadata.reader.NamespaceReader;
import net.n2oapp.framework.api.script.ScriptProcessor;
import net.n2oapp.framework.config.reader.MetadataReaderException;
import net.n2oapp.framework.config.reader.tools.ActionButtonsReaderV1;
import net.n2oapp.framework.config.reader.tools.PreFilterReaderV1Util;
import org.jdom2.Element;
import org.jdom2.Namespace;
import org.jdom2.Text;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static net.n2oapp.framework.config.reader.util.ReaderJdomUtil.*;

/**
 * Абстрактная реализация считывания контрола из метаданных
 *
 * @param <E> тип элемента, в который считавается контрол
 */
public abstract class N2oStandardControlReaderV1<E extends NamespaceUriAware> extends AbstractFactoredReader<E> implements NamespaceReader<E> {

    private ObjectMapper mapper = new ObjectMapper();

    public final static Namespace DEFAULT_EVENT_NAMESPACE_URI = Namespace.getNamespace("http://n2oapp.net/framework/config/schema/n2o-event-1.0");

    @Override
    public String getNamespaceUri() {
        return "http://n2oapp.net/framework/config/schema/n2o-control-1.0";
    }

    @SuppressWarnings("unchecked")
    protected void getControlFieldDefinition(Element field, N2oField n2oField) {
        try {
            getControlDefinition(field, n2oField);
            String domain = getAttributeString(field, "domain");
            n2oField.setDomain(domain);
            if (n2oField instanceof N2oStandardField) {
                ((N2oStandardField) n2oField).setValidations(readValidationReferences(field));
                ((N2oStandardField) n2oField).setDefaultValue(getAttributeString(field, "default-value"));
            }
            n2oField.setRequired(getAttributeString(field, "required"));
            if (getAttributeString(field, "depends-on") != null) {
                n2oField.setDependsOn(new String[]{getAttributeString(field, "depends-on")});
            }
            n2oField.setNamespaceUri(field.getNamespaceURI());
            readDefaultModel(n2oField, field.getChild("default-model", field.getNamespace()));
            n2oField.setLabelStyle(getAttributeString(field, "label-style"));
            n2oField.setStyle(getAttributeString(field, "control-style"));
            n2oField.setCssClass(getAttributeString(field, "css-class"));
            n2oField.setSrc(getAttributeString(field, "src"));
            Element dependencies = field.getChild("dependencies", field.getNamespace());
            n2oField.setDependencies(readDependencies(dependencies, field));
            readSetValues(n2oField, field.getChildren("set-value", field.getNamespace()));
            readSetValueExp(n2oField, field.getChildren("set-value-expression", field.getNamespace()));
            //actions
            ActionButtonsReaderV1 actionButtonsReaderV1 = new ActionButtonsReaderV1();
            actionButtonsReaderV1.setReaderFactory(readerFactory);
            List<N2oActionButton> buttons = getChildrenAsList(field, "actions", "button", actionButtonsReaderV1);
            if (n2oField instanceof N2oStandardField) {
                ((N2oStandardField) n2oField).setActionButtons(buttons);
                ((N2oStandardField) n2oField).setCopied(getAttributeBoolean(field, "copied"));
            }
            if (n2oField instanceof N2oListField) {
                if ("on".equalsIgnoreCase(getAttributeString(field, "cache")))
                    ((N2oListField) n2oField).setCache(true);
                else if ("off".equalsIgnoreCase(getAttributeString(field, "cache")))
                    ((N2oListField) n2oField).setCache(false);
            }
        } catch (Exception e) {
            throw new MetadataReaderException(e);
        }
    }

    private N2oField.Dependency[] readDependencies(Element element, Element fieldElement) {
        String visibilityCondition = getAttributeString(fieldElement, "dependency-condition");
        N2oField.VisibilityDependency visibilityDependency = null;
        if (visibilityCondition != null) {
            visibilityDependency = toVisibilityCondition(visibilityCondition);
        }

        int i = 0;
        N2oField.Dependency[] dependencies;
        if (visibilityDependency != null) {
            if (element == null || element.getChildren() == null)
                dependencies = new N2oField.Dependency[1];
            else
                dependencies = new N2oField.Dependency[element.getChildren().size() + 1];

            dependencies[i] = visibilityDependency;
            i++;
        } else {
            if (element == null || element.getChildren() == null)
                return null;
            dependencies = new N2oField.Dependency[element.getChildren().size()];
        }
        if (element == null || element.getChildren() == null)
            return dependencies;

        for (Element dependency : (List<Element>) element.getChildren()) {
            if (dependency.getName().equals("enabling-condition")) {
                N2oField.EnablingDependency enablingDependency = new N2oField.EnablingDependency();
                enablingDependency.setOn(dependency.getAttributeValue("on").split(","));
                enablingDependency.setValue(dependency.getValue());
                dependencies[i] = enablingDependency;
                i++;
            } else if (dependency.getName().equals("required-condition")) {
                N2oField.RequiringDependency requiringDependency = new N2oField.RequiringDependency();
                requiringDependency.setOn(dependency.getAttributeValue("on").split(","));
                requiringDependency.setValue(dependency.getValue());
                dependencies[i] = requiringDependency;
                i++;
            }
        }

        return dependencies;
    }

    private N2oField.VisibilityDependency toVisibilityCondition(String condition) {
        if (condition == null) return null;
        N2oField.VisibilityDependency res = new N2oField.VisibilityDependency();
        res.setValue(condition);
        res.setOn(
                ScriptProcessor.extractVars(condition).stream()
                        .map(f -> f.contains(".") ? f.substring(0, f.indexOf(".")) : f) //клиент не учитывает вложенные модели
                        .collect(Collectors.toList()).toArray(new String[0])
        );
        return res;
    }

    protected void readSetValueExp(N2oField n2oField, List<Element> list) {
        for (Element element : list) {
            N2oField.SetValueDependency setValue = new N2oField.SetValueDependency();
            String on = getAttributeString(element, "on");
            setValue.setOn(on != null ? on.split(",") : null);
            setValue.setValue(element.getText());
            n2oField.addDependency(setValue);
        }
    }

    protected void readSetValues(N2oField n2oField, List<Element> list) {
        for (Element element : list) {
            String ifClause = getAttributeString(element, "if");
            String thenClause = getAttributeString(element, "then");
            String elseClause = getAttributeString(element, "else");
            Element anElse = element.getChild("else", element.getNamespace());
            Map<String, String> elseClauses = toMap(anElse);
            Element anThen = element.getChild("then", element.getNamespace());
            Map<String, String> thenClauses = toMap(anThen);
            N2oField.SetValueDependency setValue = new N2oField.SetValueDependency();
            String on = getAttributeString(element, "on");
            setValue.setOn(on != null ? on.split(",") : null);
            setValue.setValue("if(" + ifClause + ") " + calculateReturnStatement(thenClause, thenClauses,
                    null) + "; else " + calculateReturnStatement(elseClause, elseClauses, " throw new Error() "));
            n2oField.addDependency(setValue);
        }
    }

    private String calculateReturnStatement(String returnClause, Map<String, String> returnClauses,
                                            String defaultReturn) {
        if (returnClause != null)
            return doLiteral(returnClause);
        else if (returnClauses != null && returnClauses.size() != 0) {
            StringBuilder res = new StringBuilder();
            res.append("res={");
            boolean begin = true;
            for (String key : returnClauses.keySet()) {
                if (!begin)
                    res.append(" ,");
                res.append(key);
                res.append(" : ");
                res.append(doLiteral(returnClauses.get(key)));
                begin = false;
            }
            res.append("}");
            return res.toString();
        }
        return defaultReturn;
    }

    private String doLiteral(String returnClause) {
        if ("true".equals(returnClause) || "false".equals(returnClause))
            return returnClause;
        else
            return "'" + returnClause + "'";
    }

    protected Map<String, String> toMap(Element anElse) {
        Map<String, String> map = null;
        if (anElse != null) {
            map = new HashMap<>();
            List<Element> children = anElse.getChildren("value", anElse.getNamespace());
            for (Element value : children) {
                map.put(getAttributeString(value, "field-id"), value.getText());
            }
        }
        return map;
    }

    private void readDefaultModel(N2oField field, Element defaultModel) {
        if (defaultModel == null) return;
        if (field instanceof N2oListField) {
            List<Element> elements = defaultModel.getChildren("value", defaultModel.getNamespace());
            Map<String, String> values = new HashMap<>();
            elements.forEach(el -> {
                String fieldId = getAttributeString(el, "field-id");
                values.put(fieldId, el.getText());
            });
            ((N2oListField) field).setDefValue(values);
        } else if (field instanceof N2oSimpleIntervalField) {
            ((N2oSimpleIntervalField) field).setBegin(getAttributeString(defaultModel, "begin"));
            ((N2oSimpleIntervalField) field).setEnd(getAttributeString(defaultModel, "end"));
        }
    }

    protected N2oField.Validations readValidationReferences(Element field) {
        Element validations = field.getChild("validations", field.getNamespace());
        N2oField.Validations inlineValidations = new N2oField.Validations();
        if (validations != null) {
            List<Element> list = validations.getChildren("validation", field.getNamespace());
            if (list != null) {
                String[] whiteList = new String[list.size()];
                int i = 0;
                for (Element el : list) {
                    whiteList[i] = getAttributeString(el, "ref-id");
                    i++;
                }
                inlineValidations.setWhiteList(whiteList);
            }
        } else return null;

        return inlineValidations;
    }

    protected void getControlDefinition(Element fieldSetElement, N2oField n2oControl) {
        String label = getAttributeString(fieldSetElement, "label");
        String visible = getAttributeString(fieldSetElement, "visible");
        n2oControl.setLabel(label);
        n2oControl.setVisible(visible);
        n2oControl.setDescription(getElementString(fieldSetElement, "description"));
        n2oControl.setId(getAttributeString(fieldSetElement, "id"));
        if (n2oControl instanceof N2oStandardField) {
            ((N2oStandardField) n2oControl).setPlaceholder(getAttributeString(fieldSetElement, "placeholder"));
        }
    }

    protected N2oListField getListFieldDefinition(Element element, N2oListField n2oListField) {
        getControlFieldDefinition(element, n2oListField);
        readAutoSelect(element, n2oListField);
        setInlineProperty(element, n2oListField);
        n2oListField.setSize(getAttributeInteger(element, "size"));

        Element options = element.getChild("options", element.getNamespace());
        if (options != null) {
            n2oListField.setOptions(readOptions(options));
            readSelectFields(options, n2oListField);
        }

        Element query = element.getChild("query", element.getNamespace());
        if (query != null) {
            readSelectFields(query, n2oListField);
            Element preFilters = query.getChild("pre-filters", query.getNamespace());
            n2oListField.setPreFilters(PreFilterReaderV1Util.getControlPreFilterListDefinition(preFilters));
        }
        n2oListField.setPopupScaling(getAttributeEnum(element, "popup-scaling", N2oListField.PopupScaling.class));
        return n2oListField;
    }

    private Map<String, String>[] readOptions(Element options) {
        Map<String, String>[] optionsMap = new HashMap[options.getChildren().size()];
        int i = 0;
        for (Element option : (List<Element>) options.getChildren()) {
            String json = ((Text) option.getContent().get(0)).getValue();
            Map<String, String> map;
            try {
                map = mapper.readValue(json, new TypeReference<Map<String, String>>() {
                });
            } catch (IOException e) {
                throw new N2oException("Can not resolve json", e);
            }
            optionsMap[i] = map;
            i++;
        }

        return optionsMap;
    }

    private void readSelectFields(Element query, N2oListField n2oListField) {
        n2oListField.setQueryId(getAttributeString(query, "query-id"));
        n2oListField.setLabelFieldId(getAttributeString(query, "label-field-id"));
        n2oListField.setValueFieldId(getAttributeString(query, "value-field-id"));
        n2oListField.setSearchFilterId(getAttributeString(query, "search-field-id"));
        n2oListField.setImageFieldId(getAttributeString(query, "image-field-id"));
        n2oListField.setIconFieldId(getAttributeString(query, "icon-field-id"));
        n2oListField.setMasterFieldId(getAttributeString(query, "master-field-id"));
        n2oListField.setDetailFieldId(getAttributeString(query, "detail-field-id"));
        n2oListField.setFormat(getAttributeString(query, "format"));
        if (n2oListField.getGroupFieldId() == null)
            n2oListField.setGroupFieldId(getAttributeString(query, "group-field-id"));
    }

    private static void readAutoSelect(Element element, N2oListField n2oListField) {
        n2oListField.setAutoselectAlone(getAttributeBoolean(element, "autoselect-alone", "autoselect"));
        n2oListField.setAutoselectFirst(getAttributeBoolean(element, "autoselect-first"));
    }

    private void setInlineProperty(Element element, N2oListField n2oListField) {
        if (n2oListField instanceof Inlineable) {
            ((Inlineable) n2oListField).setInline(getAttributeBoolean(element, "inline"));
        }
    }

    protected <T extends N2oListField> T getQueryFieldDefinition(Element element, T n2oListField) {
        getListFieldDefinition(element, n2oListField);
        n2oListField.setPlaceholder(getAttributeString(element, "placeholder"));
        return n2oListField;
    }

    protected void getTreeDefinition(Element element, Namespace namespace, N2oSelectTree selectTree) {
        selectTree.setAjax(getAttributeBoolean(element, "ajax"));
        selectTree.setSearch(getAttributeBoolean(element, "search"));
        selectTree.setCheckboxes(getAttributeBoolean(element, "checkboxes"));
        selectTree.setEnabledFieldId(getAttributeString(element, "enabled-field-id"));
        Element in = element.getChild("inheritance-nodes", namespace);
        if (in != null) {
            selectTree.setQueryId(getAttributeString(in, "query-id"));
            selectTree.setIconFieldId(getAttributeString(in, "icon-field-id"));
            selectTree.setParentFieldId(getAttributeString(in, "parent-field-id"));
            selectTree.setLabelFieldId(getAttributeString(in, "label-field-id"));
            selectTree.setMasterFieldId(getAttributeString(in, "master-field-id"));
            selectTree.setDetailFieldId(getAttributeString(in, "detail-field-id"));
            selectTree.setValueFieldId(getAttributeString(in, "value-field-id"));
            selectTree.setSearchFilterId(getAttributeString(in, "search-field-id"));
            selectTree.setEnabledFieldId(getAttributeString(in, "enabled-field-id"));
            selectTree.setHasChildrenFieldId(getAttributeString(in, "has-children-field-id"));
            Element preFilters = in.getChild("pre-filters", namespace);
            selectTree.setPreFilters(PreFilterReaderV1Util.getControlPreFilterListDefinition(preFilters));
        }
    }
}
